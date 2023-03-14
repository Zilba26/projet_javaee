package fr.zilba.projet_javaee.dao;

import fr.zilba.projet_javaee.beans.BeanException;
import fr.zilba.projet_javaee.beans.Gender;
import fr.zilba.projet_javaee.beans.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    private final DaoFactory daoFactory;

    StudentDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void add(Student student) throws BeanException {
        if (student.getFirstName().isEmpty()) {
            if (student.getLastName().isEmpty()) {
                throw new BeanException("Le prénom et le nom ne peuvent pas être vide");
            }
            throw new BeanException("Le prénom ne peut pas être vide");
        }
        if (student.getLastName().isEmpty()) {
            throw new BeanException("Le nom ne peut pas être vide");
        }
        Connection connexion;
        PreparedStatement preparedStatement;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement("INSERT INTO `students`(`first_name`, `last_name`, `gender`, `last_place`, `last_formation`) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getGender().toString());
            preparedStatement.setString(4, student.getLastPlace());
            preparedStatement.setString(5, student.getLastFormation());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        Connection connexion;
        PreparedStatement preparedStatement;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement("DELETE FROM `students`");

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeTeam(int teamId, int studentId) {
        Connection connexion;
        PreparedStatement preparedStatement;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement("UPDATE `students` SET `team_id` = ? WHERE `id` = ?");

            String teamIdString = null;
            if (teamId != 0) {
                teamIdString = String.valueOf(teamId);
            }
            preparedStatement.setString(1, teamIdString);
            preparedStatement.setString(2, String.valueOf(studentId));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> list() {
        List<Student> students = new ArrayList<>();
        Connection connexion;
        Statement statement;
        ResultSet result;

        try {
            connexion = daoFactory.getConnection();
            statement = connexion.createStatement();
            result = statement.executeQuery("SELECT * FROM students ORDER BY last_name, first_name;");

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

                Student student = new Student(id, firstName, lastName, gender, lastPlace, lastFormation, teamId);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

}