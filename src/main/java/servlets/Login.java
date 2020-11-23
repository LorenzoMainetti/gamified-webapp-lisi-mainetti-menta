package servlets;

import entities.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;
import services.UserService;

import java.io.IOException;
import java.io.IOException;
import java.util.Random;

@WebServlet("/Login")
public class Login extends HttpServlet {

   @EJB(name = "src/main/java/services/UserService.java")
    private UserService usrService;

    private void writeUser (String email, String username, String password) {
       usrService.insertUser(username, email,password,true);
    }

    boolean checkPassword(String password) {
        return true;
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = StringEscapeUtils.escapeJava(request.getParameter("email"));
        String password = StringEscapeUtils.escapeJava(request.getParameter("password"));

        if (checkPassword(password) && email.length()>3) {
            writeUser(email,"nuovo", password);

            response.setContentType("text/plain");
            response.getWriter().println("scritto. ciao " + email.toUpperCase());
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
