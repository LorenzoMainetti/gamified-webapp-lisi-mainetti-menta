package it.polimi.db2.servlets.admin;

import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Question;
import it.polimi.db2.services.*;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebServlet("/CreateQuestionnaire")
@MultipartConfig
public class CreateQuestionnaire extends HttpServlet {

    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2.entities.services/QuestionService")
    private QuestionService questionService;
    @EJB(name = "it.polimi.db2.entities.services/AdminService")
    private AdminService adminService;

    private LocalDate getDateFromRequestParameter(String param) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //convert String to LocalDate
        return LocalDate.parse(param, formatter);
    }

    private Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO ESCAPE WHEN READING CHARACTERS
        String productName = request.getParameterValues("name")[0];
        String description = request.getParameterValues("description")[0];

        String sDate = request.getParameterValues("date")[0];
        Date date = null;

            //TODO check if it the right date format, if not convert it
            date = asDate(getDateFromRequestParameter(sDate));


        String adminId = (String) request.getSession().getAttribute("admin");
        Admin admin = adminService.getAdmin(adminId);



        //TODO the admin still needs to upload the image, if he wants

        List<String> mandatoryQuestionsString = Arrays.asList(request.getParameterValues("man[]"));


        Product prod = productService.insertProduct(productName, date, description, admin);
        List <Question> questions = questionService.getAllQuestions(mandatoryQuestionsString, prod);
        questionService.updateProductQuestiosn(prod, questions);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
