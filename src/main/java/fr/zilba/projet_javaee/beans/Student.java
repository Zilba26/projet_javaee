package fr.zilba.projet_javaee.beans;

public class Student {

    private String firstName;
    private String lastName;
    private Gender gender;
    private String lastPlace;
    private String lastFormation;

    public Student(String firstName, String lastName, Gender gender, String lastPlace, String lastFormation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.lastPlace = lastPlace;
        this.lastFormation = lastFormation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getLastPlace() {
        return lastPlace;
    }

    public void setLastPlace(String lastPlace) {
        this.lastPlace = lastPlace;
    }

    public String getLastFormation() {
        return lastFormation;
    }

    public void setLastFormation(String lastFormation) {
        this.lastFormation = lastFormation;
    }

    public void print() {
        System.out.println("Student :");
        System.out.println("    First name : " + this.firstName);
        System.out.println("    Last name : " + this.lastName);
        System.out.println("    Gender : " + this.gender);
        System.out.println("    Last place : " + this.lastPlace);
        System.out.println("    Last formation : " + this.lastFormation);
    }
}
