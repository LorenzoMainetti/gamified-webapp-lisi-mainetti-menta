package it.polimi.db2.servlets.admin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.db2.admin.InspectionPageContent;
import it.polimi.db2.admin.PastQuestionnairePageContent;
import it.polimi.db2.entities.*;
import it.polimi.db2.exception.ProductNotFoundException;
import it.polimi.db2.services.*;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.persistence.Tuple;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Link;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/GetInspectionData")
public class GetInspectionData extends HttpServlet {

    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2.entities.services/QuestionService")
    private QuestionService questionService;
    @EJB(name = "it.polimi.db2.entities.services/AnswerService")
    private AnswerService answerService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    boolean checkProductId (int productId) {
        return productId > 0;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //int productId = (Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("pid"))));
        int productId = 1; //hardcoded, uncomment previous line to proper work in general use case
        if(checkProductId(productId)) {
            try {

                //TODO send also inspected product (name, date, description and image)

                //String productJson = productService.getProductToGson(productId); //TODO IT CRASHED
                List<String> usersWhoSubmitted, usersWhoCanceled;
                Map<String, List<String>> answersForEachUser = new HashMap<>();

                usersWhoSubmitted = new LinkedList<>();
                usersWhoCanceled = new LinkedList<>();

                //get lists
                Product product = productService.getProduct(productId);
                productService.getProductUsers(product, false).forEach( u -> {
                    usersWhoCanceled.add(u.getUsername());
                });
                productService.getProductUsers(product, true).forEach( u -> {
                    usersWhoSubmitted.add(u.getUsername());
                });

                //get all the questions for current product

                List<String> questions = product.getQuestionsText();


                for (String s : usersWhoSubmitted) {

                    List <Answer> answersFromUser = productService.getUserAnswers(product, s);

                    //get only the text
                    List<String> answers = new LinkedList<>();
                    for (Answer answer : answersFromUser) {
                        answers.add(answer.getText());
                    }

                    answersForEachUser.put(s, answers );
                }

                String encoded = Base64.getEncoder().encodeToString(product.getImage());
                InspectionPageContent content = new InspectionPageContent(usersWhoSubmitted, usersWhoCanceled, answersForEachUser, questions,
                        product.getName(), product.getDescription(), encoded, product.getDate());

                String jsonResponse = new Gson().toJson(content);

                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);

                out.write(jsonResponse);



            } catch (Exception e) {
               //todo mentaaaahhh 🌱
            }
        }


    }
}
