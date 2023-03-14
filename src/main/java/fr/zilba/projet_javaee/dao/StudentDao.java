package fr.zilba.projet_javaee.dao;

import fr.zilba.projet_javaee.beans.BeanException;
import fr.zilba.projet_javaee.beans.Student;

import java.util.List;

public interface StudentDao {
    void add( Student student ) throws BeanException;
    void deleteAll();
    void changeTeam(int teamId, int studentId);
    default void changeTeam(int teamId, Student student) {
        changeTeam(teamId, student.getId());
    }

    List<Student> list();
}