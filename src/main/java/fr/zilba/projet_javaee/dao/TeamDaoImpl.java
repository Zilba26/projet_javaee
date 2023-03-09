package fr.zilba.projet_javaee.dao;

import java.sql.*;

public class TeamDaoImpl implements TeamDao {
    private DaoFactory daoFactory;

    TeamDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void changeTeamNb(int nbTeams) {
        Connection connexion = null;
        Statement statement = null;
        ResultSet resultat = null;

        try {
            connexion = daoFactory.getConnection();
            statement = connexion.createStatement();
            resultat = statement.executeQuery("SELECT COUNT(*) FROM `teams`");

            if (resultat.next()) {
                int oldTeamNb = resultat.getInt(1);
                if (oldTeamNb < nbTeams) {
                    for (int i = oldTeamNb; i < nbTeams; i++) {
                        statement.executeUpdate("INSERT INTO `teams`(`id`) VALUES (" + (i + 1) + ")");
                    }
                } else if (oldTeamNb > nbTeams) {
                    for (int i = oldTeamNb; i > nbTeams; i--) {
                        statement.executeUpdate("UPDATE `students` SET `team_id` = NULL WHERE `team_id` = " + i);
                        statement.executeUpdate("DELETE FROM `teams` WHERE `id` = " + i);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getTeamNb() {
        Connection connexion = null;
        Statement statement = null;
        ResultSet resultat = null;

        int nbTeams = 0;

        try {
            connexion = daoFactory.getConnection();
            statement = connexion.createStatement();
            resultat = statement.executeQuery("SELECT COUNT(*) FROM `teams`");

            if (resultat.next()) {
                nbTeams = resultat.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nbTeams;
    }
}
