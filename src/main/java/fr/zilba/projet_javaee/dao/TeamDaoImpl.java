package fr.zilba.projet_javaee.dao;

import fr.zilba.projet_javaee.beans.Gender;
import fr.zilba.projet_javaee.beans.Student;
import fr.zilba.projet_javaee.beans.Team;

import java.sql.*;
import java.util.*;

public class TeamDaoImpl implements TeamDao {
    private DaoFactory daoFactory;

    TeamDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void addTeam(String teamName) {
        Connection connexion = null;
        Statement statement = null;

        try {
            connexion = daoFactory.getConnection();
            statement = connexion.createStatement();
            statement.executeUpdate("INSERT INTO `teams`(`name`) VALUES ('" + teamName + "')");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTeam(int teamId) {
        Connection connexion = null;
        Statement statement = null;

        try {
            connexion = daoFactory.getConnection();
            statement = connexion.createStatement();
            statement.executeUpdate("UPDATE `students` SET `team_id` = NULL WHERE `team_id` = " + teamId);
            statement.executeUpdate("DELETE FROM `teams` WHERE `id` = " + teamId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeTeamName(int teamId, String teamName) {
        Connection connexion = null;
        Statement statement = null;

        try {
            connexion = daoFactory.getConnection();
            statement = connexion.createStatement();
            statement.executeUpdate("UPDATE `teams` SET `name` = '" + teamName + "' WHERE `id` = " + teamId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Team> list() {
        Connection connexion = null;
        Statement statement = null;
        ResultSet result = null;

        List<Team> teams = new ArrayList<>();

        try {
            connexion = daoFactory.getConnection();
            statement = connexion.createStatement();
            result = statement.executeQuery("SELECT * FROM `teams` ORDER BY `id`");

            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");

                Team team = new Team(id, name);
                teams.add(team);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }

    @Override
    public void addStudentsAuto(Integer criteria) {
        Connection connexion;
        Statement statement;
        ResultSet result;

        try {
            List<Team> teams = list();
            List<Student> students = new ArrayList<>();
            Map<Integer, Integer> studentsByTeam = new HashMap<>();
            for (Team team : teams) {
                studentsByTeam.put(team.getId(), 0);
            }
            int minNbPerTeam = 0;

            connexion = daoFactory.getConnection();
            statement = connexion.createStatement();
            result = statement.executeQuery("SELECT * FROM `students`");
            int nbStudents = 0;

            while (result.next()) {
                nbStudents++;
                int id = result.getInt("id");
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                Gender gender = Gender.valueOf(result.getString("gender"));
                String lastPlace = result.getString("last_place");
                String lastFormation = result.getString("last_formation");
                String teamIdString = result.getString("team_id");
                Integer teamId = null;
                if (teamIdString != null) {
                    teamId = Integer.parseInt(teamIdString);
                }

                if (teamId != null) {
                    studentsByTeam.replace(teamId, studentsByTeam.get(teamId) + 1);
                    if (studentsByTeam.get(teamId) > minNbPerTeam) {
                        minNbPerTeam = studentsByTeam.get(teamId);
                    }
                } else {
                    Student student = new Student(id, firstName, lastName, gender, lastPlace, lastFormation, teamId);
                    students.add(student);
                }
            }

            switch (criteria) {
                case 0:
                    Collections.shuffle(students);
                    break;
                case 1:
                    students.sort(Comparator.comparing(Student::getLastName));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + criteria);
            }

            int minMaxStudentsPerTeam = nbStudents / teams.size();
            int reste = nbStudents % teams.size();
            for (Team team : teams) {
                while (studentsByTeam.get(team.getId()) < minMaxStudentsPerTeam + (reste > teams.indexOf(team) ? 1 : 0)) {
                    Student randomStudent = students.remove(0);
                    statement.executeUpdate("UPDATE `students` SET `team_id` = " + team.getId() + " WHERE `id` = " + randomStudent.getId());
                    studentsByTeam.replace(team.getId(), studentsByTeam.get(team.getId()) + 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
