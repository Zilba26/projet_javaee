package fr.zilba.projet_javaee.dao;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DaoFactory {

    //public static final ResourceBundle CONFIGURATION = ResourceBundle.getBundle("config");
    private final String url;
    private final String username;
    private final String password;

    DaoFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DaoFactory getInstance() throws URISyntaxException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        String dbName = CONFIGURATION.getString("DATABASE_NAME");
//        String username = CONFIGURATION.getString("USERNAME");
//        String password = CONFIGURATION.getString("PASSWORD");

        DaoFactory instance = new DaoFactory(
                "jdbc:mysql://localhost:3306/" + "projet_java_ee", "root", "");
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // Récupération du Dao
    public StudentDao getStudentDao() {
        return new StudentDaoImpl(this);
    }
}