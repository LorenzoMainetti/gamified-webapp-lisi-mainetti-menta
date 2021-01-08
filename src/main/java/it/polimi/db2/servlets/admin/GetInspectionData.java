package it.polimi.db2.servlets.admin;

import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.User;
import it.polimi.db2.exception.ProductNotFoundException;
import it.polimi.db2.services.*;
import jakarta.ejb.EJB;
import jakarta.persistence.Tuple;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Link;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int productId = Integer.parseInt(request.getParameter("pid"));

        try {

            //String productJson = productService.getProductToGson(productId); //TODO IT CRASHED
            List<String> usersWhoSubmitted, usersWhoCanceled;
            Map<String, Map<String, String>> qnaForEachUser;

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


            /*
            USERS WHO CANCELED THE QUESTIONNAIRE
            
             */

            /*
            QUESTIONNAIRE Q & A FOR EACH USER
             */

            System.out.println();

        } catch (Exception e) {
            //todo mentaaaahhh 🌱
        }
    }
}
