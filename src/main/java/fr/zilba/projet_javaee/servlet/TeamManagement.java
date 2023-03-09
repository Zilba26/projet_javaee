package fr.zilba.projet_javaee.servlet;

import fr.zilba.projet_javaee.beans.Student;
import fr.zilba.projet_javaee.dao.DaoFactory;
import fr.zilba.projet_javaee.dao.StudentDao;
import fr.zilba.projet_javaee.dao.TeamDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@WebServlet(name = "TeamManagement", value = "/team-management")
public class TeamManagement extends HttpServlet {

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

        int teamsNb = teamDao.getTeamNb();
        request.setAttribute("teamsNb", teamsNb);

        this.getServletContext().getRequestDispatcher("/WEB-INF/team_management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Student> students = studentDao.list();
        request.setAttribute("students", students);

        String teamsNb = request.getParameter("teams_nb");
        if (teamsNb != null) {
            teamDao.changeTeamNb(Integer.parseInt(teamsNb));
        }

        String teamId = request.getParameter("teamId");
        System.out.println("teamId = " + teamId);
        if (teamId != null) {
            if (teamId.equals("null")) {
                teamId = "0";
            }
            String studentId = request.getParameter("studentId");
            System.out.println("teamId = " + teamId);
            System.out.println("studentId = " + studentId);
            studentDao.changeTeam(Integer.parseInt(teamId), Integer.parseInt(studentId));
        }

        this.doGet(request, response);
        //this.getServletContext().getRequestDispatcher("/WEB-INF/team_management.jsp").forward(request, response);
    }
}
