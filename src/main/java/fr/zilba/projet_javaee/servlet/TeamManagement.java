package fr.zilba.projet_javaee.servlet;

import com.opencsv.CSVWriter;
import fr.zilba.projet_javaee.beans.Student;
import fr.zilba.projet_javaee.beans.Team;
import fr.zilba.projet_javaee.dao.DaoFactory;
import fr.zilba.projet_javaee.dao.StudentDao;
import fr.zilba.projet_javaee.dao.TeamDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;

@WebServlet(name = "TeamManagement", value = "/team-management")
public class TeamManagement extends HttpServlet {

    private static final int TAILLE_TAMPON = 10240;
    private StudentDao studentDao;
    private TeamDao teamDao;

    @Override
    public void init() {
        DaoFactory daoFactory;
        try {
            daoFactory = DaoFactory.getInstance();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.studentDao = daoFactory.getStudentDao();
        this.teamDao = daoFactory.getTeamDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Student> students = studentDao.list();
        request.setAttribute("students", students);

        List<Team> teams = teamDao.list();
        request.setAttribute("teams", teams);

        this.getServletContext().getRequestDispatcher("/WEB-INF/team_management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String teamId = request.getParameter("teamId");
        if (teamId != null) {
            if (teamId.equals("null")) {
                teamId = "0";
            }
            String deleteTeamButton = request.getParameter("delete_team");
            String changeTeamNameButton = request.getParameter("change_team_name");
            String autoCompo = request.getParameter("auto_compo");

            if (deleteTeamButton != null) {
                teamDao.deleteTeam(Integer.parseInt(teamId));
            } else if (changeTeamNameButton != null) {
                String teamName = request.getParameter("teamName");
                teamDao.changeTeamName(Integer.parseInt(teamId), teamName);
            } else if (autoCompo != null) {
                teamDao.addStudentsAuto();
            } else {
                String studentId = request.getParameter("studentId");
                studentDao.changeTeam(Integer.parseInt(teamId), Integer.parseInt(studentId));
            }
        }

        String addTeamButton = request.getParameter("add_team");
        if (addTeamButton != null) {
            List<Team> teams = teamDao.list();
            teamDao.addTeam(teams.size() + 1);
        }

        String exportCsvButton = request.getParameter("export_csv");
        if (exportCsvButton != null) {
            List<Student> students = studentDao.list();
            List<Team> teams = teamDao.list();
            exportCsv(students, teams, "C:\\downloadFilesJavaEE\\students_with_teams.csv");
            //TODO : configurer le chemin
        }

        this.doGet(request, response);
        //this.getServletContext().getRequestDispatcher("/WEB-INF/team_management.jsp").forward(request, response);
    }

    public void exportCsv(List<Student> students, List<Team> teams, String fileNameWithPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameWithPath))) {
            // Écrire l'en-tête du fichier CSV
            writer.write("nom,prenom,nom_equipe\n");

            // Parcourir les étudiants et écrire chaque ligne dans le fichier CSV
            for (Student student : students) {
                if (student.getTeamId() != null) {
                    writer.write(student.getLastName() + "," + student.getFirstName() + ",");

                    // Trouver le nom de l'équipe de l'étudiant
                    String nomEquipe = "";
                    for (Team team : teams) {
                        if (team.getId() == student.getTeamId()) {
                            nomEquipe = team.getName();
                            break;
                        }
                    }
                    writer.write(nomEquipe);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
