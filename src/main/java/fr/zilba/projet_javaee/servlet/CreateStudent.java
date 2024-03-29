package fr.zilba.projet_javaee.servlet;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import fr.zilba.projet_javaee.beans.BeanException;
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

    @Override
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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        String firstSubmit = request.getParameter("classic_submit");
        String csv_submit = request.getParameter("csv_submit");

        if (firstSubmit != null) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String gender = request.getParameter("gender");
            String lastPlace = request.getParameter("lastPlace");
            String lastFormation = request.getParameter("lastFormation");
            Student student = new Student(null, firstName, lastName, Gender.valueOf(gender), lastPlace, lastFormation, null);
            try {
                studentDao.add(student);
            } catch (BeanException e) {
                request.setAttribute("error", e.getMessage());
                request.setAttribute("student", student);
            }
        }

        if (csv_submit != null) {
            try {
                Part part = request.getPart("fichier");
                CSVReader reader = new CSVReader(new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8));

                List<String[]> rows = reader.readAll();
                int studentImported = 0;

                //studentDao.deleteAll();

                for (String[] row : rows) {
                    for (String cell : row) {
                        List<String> cells = Arrays.asList(cell.split(";"));
                        Student student = new Student(null, cells.get(0), cells.get(1), Gender.valueOf(cells.get(2)), cells.get(3), cells.get(4), null);
                        studentDao.add(student);
                        studentImported++;
                    }
                }

                request.setAttribute("studentImported", studentImported);

            } catch (BeanException e) {
                e.printStackTrace();
                request.setAttribute("error", "Une erreur a lieu dans les informations des étudiants (" + e.getMessage() + ")");
            } catch (CsvException e) {
                e.printStackTrace();
                request.setAttribute("error", "Impossible d'importer le fichier. Vérifiez le format");
            }
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/create_student.jsp").forward(request, response);
    }
}