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
import java.util.ResourceBundle;

@WebServlet(name = "TeamManagement", value = "/team-management")
public class TeamManagement extends HttpServlet {

    public static final ResourceBundle CONFIGURATION = ResourceBundle.getBundle("config");
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
                int criteria = Integer.parseInt(request.getParameter("criteria"));
                teamDao.addStudentsAuto(criteria);
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
            String exportPath = CONFIGURATION.getString("EXPORT_PATH_CSV");
            String exportNameFile = CONFIGURATION.getString("EXPORT_NAME_FILE_CSV");
            boolean goodExport = exportCsv(students, teams, exportPath + "\\" + exportNameFile);
            if (goodExport) {
                request.setAttribute("exportCsv", "Export CSV réussi");
            } else {
                request.setAttribute("exportCsv", "Export CSV échoué (vérifiez le fichier de configuration)");
            }
        }

        this.doGet(request, response);
        //this.getServletContext().getRequestDispatcher("/WEB-INF/team_management.jsp").forward(request, response);
    }

    public boolean exportCsv(List<Student> students, List<Team> teams, String fileNameWithPath) {
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
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
