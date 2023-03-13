package fr.zilba.projet_javaee.dao;

import fr.zilba.projet_javaee.beans.Gender;
import fr.zilba.projet_javaee.beans.Student;
import fr.zilba.projet_javaee.beans.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void addStudentsAuto() {
        Connection connexion = null;
        Statement statement = null;
        ResultSet result = null;

        try {
            List<Team> teams = list();
            List<Student> students = new ArrayList<>();
            Map<Integer, Integer> studentsByTeam = new HashMap<>();
            for(int i = 0; i < teams.size(); i++){
                studentsByTeam.put(teams.get(i).getId(), 0);
            }
            int minNbPerTeam = 0;

            connexion = daoFactory.getConnection();
            statement = connexion.createStatement();
            result = statement.executeQuery("SELECT * FROM `students`");

            while (result.next()) {
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

            int i = 0;
            for (int j = students.size() ; j > 0 ; j--) {

                int maxLoop = 0;

                while (studentsByTeam.get(teams.get(i).getId()) >= minNbPerTeam) {
                    i++;
                    if (i >= teams.size()) {
                        i = 0;
                        minNbPerTeam++;
                    }
                    maxLoop++;
                    if (maxLoop > 10000) {
                        throw new RuntimeException("Erreur dans la répartition des étudiants, Loop infini");
                    }
                }

                Student randomStudent = students.get((int) (Math.random() * j));
                students.remove(randomStudent);

                statement.executeUpdate("UPDATE `students` SET `team_id` = " + teams.get(i).getId() + " WHERE `id` = " + randomStudent.getId());
                studentsByTeam.replace(teams.get(i).getId(), studentsByTeam.get(teams.get(i).getId()) + 1);
                i++;
                if (i >= teams.size()) {
                    i = 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
