package fr.zilba.projet_javaee.dao;

import fr.zilba.projet_javaee.beans.Team;

import java.util.List;

public interface TeamDao {

    void addTeam(String teamName);

    default void addTeam(int nbTeam) {
        addTeam("Team " + nbTeam);
    }

    void deleteTeam(int teamId);

    void changeTeamName(int teamId, String teamName);

    List<Team> list();

    default void addStudentsAuto() {
        addStudentsAuto(0);
    };

    void addStudentsAuto(Integer criteria);
}
