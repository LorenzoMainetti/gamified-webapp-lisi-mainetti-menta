package it.polimi.db2.servlets;

import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Question;
import it.polimi.db2.entities.User;
import it.polimi.db2.services.AnswerService;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.UserService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/SubmitAnswer")
@MultipartConfig
public class SubmitAnswer extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2.entities.services/AnswerService")
    private AnswerService answerService;
    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<String> mandatoryAnswers = Arrays.asList(request.getParameterValues("man[]"));
        System.out.println(mandatoryAnswers);
        String username = (String) request.getSession().getAttribute("user");

        //check validity (no bad words) or reject and ban user
        for(String answer: mandatoryAnswers)
            if(answerService.multipleWordsCheck(answer)) {
                userService.banUser(username);
                displayBanError(response);
                return;

            }


        /*Enum ... = (String) request.getParameter("gender");
        (int) request.getParameter("age")
                if () */

        User user = userService.getUser(username);
        //if the user has not been banned, save his answers in the database
        if(!user.isBanned()) {
            Product product = productService.getProductOfTheDay();
            List<Question> questions = product.getQuestions();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            response.getWriter().println("funziona");

            for (int i = 0; i < mandatoryAnswers.size(); i++) {
                answerService.createAnswer(user, questions.get(i), mandatoryAnswers.get(i));
            }

            String age = request.getParameterValues("age")[0];
            String gender = request.getParameterValues("gender")[0];
            String expertise = request.getParameterValues("expertise level")[0];
            int index = mandatoryAnswers.size();
            answerService.createAnswer(user, questions.get(index), age);
            answerService.createAnswer(user, questions.get(index + 1), gender);
            answerService.createAnswer(user, questions.get(index + 2), expertise);
        }

        else displayBanError(response);
    }

    private void displayBanError(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("You've been banned. Unable to process your request");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



    }
}
