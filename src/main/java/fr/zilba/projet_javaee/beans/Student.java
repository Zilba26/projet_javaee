package fr.zilba.projet_javaee.beans;

public class Student {

    private Integer id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String lastPlace;
    private String lastFormation;
    private Integer teamId;

    public Student(Integer id, String firstName, String lastName, Gender gender, String lastPlace, String lastFormation, Integer teamId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.lastPlace = lastPlace;
        this.lastFormation = lastFormation;
        this.teamId = teamId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
    public boolean hasTeam() {
        return this.teamId != null;
    }

    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
