package it.polimi.db2.servlets.admin;

import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Question;
import it.polimi.db2.services.*;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebServlet("/CreateQuestionnaire")
public class CreateQuestionnaire extends HttpServlet {

    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2.entities.services/QuestionService")
    private QuestionService questionService;
    @EJB(name = "it.polimi.db2.entities.services/AdminService")
    private AdminService adminService;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO ESCAPE WHEN READING CHARACTERS
        String productName = request.getParameterValues("name")[0];
        String description = request.getParameterValues("description")[0];

        String sDate = request.getParameterValues("date")[0];
        Date date = null;
        try {
            //TODO check if it the right date format, if not convert it
            date = new SimpleDateFormat("yyyy-MM-gg").parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String adminId = (String) request.getSession().getAttribute("admin");
        Admin admin = adminService.getAdmin(adminId);

        Product product = productService.insertProduct(productName, date, description, admin);

        //TODO the admin still needs to upload the image, if he wants

        List<String> adminInputs = Arrays.asList(request.getParameterValues("questions[]"));
        //create mandatory questions
        List<Question> questions = new ArrayList<>();
        for (String input : adminInputs) {
            questions.add(questionService.insertQuestion(product, input));
        }
        //add optional stat questions
        questions = questionService.addStatQuestions(questions);
        product.setQuestions(questions);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
