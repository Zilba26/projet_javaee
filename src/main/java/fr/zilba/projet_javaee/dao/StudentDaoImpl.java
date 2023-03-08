package fr.zilba.projet_javaee.dao;

import fr.zilba.projet_javaee.beans.Gender;
import fr.zilba.projet_javaee.beans.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    private DaoFactory daoFactory;

    StudentDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void add(Student student) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement("INSERT INTO `students`(`first_name`, `last_name`, `gender`, `last_place`, `last_formation`) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            System.out.println(student.getGender().toString());
            preparedStatement.setString(3, student.getGender().toString());
            preparedStatement.setString(4, student.getLastPlace());
            preparedStatement.setString(5, student.getLastFormation());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Student> list() {
        List<Student> students = new ArrayList<Student>();
        Connection connexion = null;
        Statement statement = null;
        ResultSet resultat = null;

        try {
            connexion = daoFactory.getConnection();
            statement = connexion.createStatement();
            resultat = statement.executeQuery("SELECT * FROM students;");

            while (resultat.next()) {
                String firstName = resultat.getString("first_name");
                String lastName = resultat.getString("last_name");
                Gender gender = Gender.valueOf(resultat.getString("gender"));
                System.out.println(gender);
                String lastPlace = resultat.getString("last_place");
                String lastFormation = resultat.getString("last_formation");

                Student student = new Student(firstName, lastName, gender, lastPlace, lastFormation);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

}