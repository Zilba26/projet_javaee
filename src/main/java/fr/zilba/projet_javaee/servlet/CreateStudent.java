package fr.zilba.projet_javaee.servlet;

import java.io.*;
import java.net.URISyntaxException;

import com.opencsv.CSVReader;
import fr.zilba.projet_javaee.beans.Gender;
import fr.zilba.projet_javaee.beans.Student;
import fr.zilba.projet_javaee.dao.DaoFactory;
import fr.zilba.projet_javaee.dao.StudentDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@MultipartConfig
@WebServlet(name = "CreateStudent", value = "/create-student")
public class CreateStudent extends HttpServlet {

    private StudentDao studentDao;

    public void init() {
        DaoFactory daoFactory;
        try {
            daoFactory = DaoFactory.getInstance();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.studentDao = daoFactory.getStudentDao();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        this.getServletContext().getRequestDispatcher("/WEB-INF/create_student.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String firstSubmit = request.getParameter("classic_submit");
        String csv_submit = request.getParameter("csv_submit");

        if (firstSubmit != null) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String gender = request.getParameter("gender");
            String lastPlace = request.getParameter("lastPlace");
            String lastFormation = request.getParameter("lastFormation");
            Student student = new Student(firstName, lastName, Gender.valueOf(gender), lastPlace, lastFormation);
            studentDao.add(student);
        }

        if (csv_submit != null) {
            try {
                Part part = request.getPart("fichier");
                System.out.println(part.getSubmittedFileName());
                CSVReader reader = new CSVReader(new InputStreamReader(part.getInputStream()));

                String[] nextLine;

                while ((nextLine = reader.readNext()) != null) {

                    for (var e : nextLine) {
                        System.out.format("%s ", e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/create_student.jsp").forward(request, response);
    }
}