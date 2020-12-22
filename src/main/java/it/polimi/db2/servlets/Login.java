package it.polimi.db2.servlets;

import it.polimi.db2.entities.User;
import it.polimi.db2.services.UserService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet("/Login")
public class Login extends HttpServlet {

   @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService usrService;


    boolean checkPassword(String password) {
        return true;
    }

    void sendError(HttpServletResponse response, String errorText) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(errorText);
    }
    boolean isEmailValid(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
    boolean isUsernameValid(String username) {
        return username.length()<32 && username.length() > 3;
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = StringEscapeUtils.escapeJava(request.getParameter("email"));
        String password = StringEscapeUtils.escapeJava(request.getParameter("password"));
        String username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        String isSignUp = StringEscapeUtils.escapeJava(request.getParameter("SIGNUP"));

        //CHECK NULLITY
        if (username == null || password == null) {
            //TODO invalid request parameters
            sendError(response, "some fields are missing");
            return;
        }




        //SIGNUP OR LOGIN

        if (isSignUp != null && isSignUp.equals("true")) {

            if (email == null) {
                sendError(response, "missing email address");
                return;
            }
            //Check if valid (synthax, length)
            if (!(isEmailValid(email) && isUsernameValid(username))) {
                sendError(response,"invalid username or password format");
                return;
            }
            //try to register a new user
            try {
                usrService.insertUser(username,email,password,false);
                request.getSession().setAttribute("user", username);
                String path = getServletContext().getContextPath() + "/homepage.html";
                response.sendRedirect(path);
            }
            catch (PersistenceException | EJBTransactionRolledbackException e) {
                //TODO send internal server error

                if (e.getCause().getCause().getMessage().contains("Duplicate entry")) {
                    sendError(response, "username already taken");
                }
                else {
                    sendError(response, "internal server error");
                }

            }

            //USER ALREADY EXISTING

            return;
        }
        else if (isSignUp == null) { // *** LOGIN existing user
            //Check if valid (synthax, length)
            if (!isUsernameValid(username)) {
                sendError(response,"invalid username or password format");
                return;
            }

            try {
                User credentialCheckResultUser = usrService.checkCredentials(username, password);
                request.getSession().setAttribute("user", credentialCheckResultUser.getUsername());
                String path = getServletContext().getContextPath() + "/homepage.html";
                response.sendRedirect(path);
            }
            catch (Exception e) {
                sendError(response, e.getMessage());
            }
        }
        else {
            //TODO generic error
        }



        /*
        if (checkPassword(password) && email.length()>3) {
            //writeUser(email,"nuovo", password);

            response.setContentType("text/plain");
            response.getWriter().println("scritto. ciao " + email.toUpperCase());
        }
 */


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
