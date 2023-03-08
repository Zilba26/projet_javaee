package fr.zilba.projet_javaee.dao;

import fr.zilba.projet_javaee.beans.Student;

import java.util.List;

public interface StudentDao {
    void add( Student student );
    List<Student> list();
}