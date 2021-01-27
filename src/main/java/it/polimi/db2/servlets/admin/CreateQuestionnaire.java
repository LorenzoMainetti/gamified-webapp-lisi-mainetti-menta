package it.polimi.db2.servlets.admin;

import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Question;
import it.polimi.db2.services.*;
import jakarta.ejb.EJB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

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

    private String checkNameDescription(String name, String description) {
        if (name == null || description == null) {
            return "Some fields are missing";
        } else if (name.length() < 2 || description.length() < 2) {
            return "Name or length is too short.";

        } else {
            return "GO_FLIGHT";
        }
    }

    /**
     * @param request  contains all parameters from Create Questionnaire form
     * @param response
     * @author Ale Lisi & Main
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO ESCAPE WHEN READING CHARACTERS
        String productName = StringEscapeUtils.escapeJava(request.getParameterValues("name")[0]);
        String description = StringEscapeUtils.escapeJava(request.getParameterValues("description")[0]);

        if (checkNameDescription(productName, description) == "GO_FLIGHT") {
            String sDate = request.getParameterValues("date")[0];
            Date date = null;

            //TODO check if it the right date format, if not convert it
            try {
                date = asDate(getDateFromRequestParameter(sDate));
            }
            catch(DateTimeParseException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain");
                response.getWriter().println("Please insert a valid date");
                return;
            }
            if(!adminService.checkQuestionnaireValidity(date)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain");
                response.getWriter().println("You can not insert a product for the selected date");
                return;
            }


            String adminId = (String) request.getSession().getAttribute("admin");
            Admin admin = adminService.getAdmin(adminId);


            List<String> mandatoryQuestionsString = Arrays.asList(request.getParameterValues("man[]"));

            if (mandatoryQuestionsString.size() < 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain");
                response.getWriter().println("At least one mandatory question is required.");
                return;
            }
            if (!checkQuestionsLength(mandatoryQuestionsString)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain");
                response.getWriter().println("Please insert valid questions.");
                return;
            }
            //questions are good
            if (checkNoDuplicateQuestions(mandatoryQuestionsString)) {
                Product prod = productService.insertProduct(productName, date, description, admin);
                List<Question> questions = questionService.getAllQuestions(mandatoryQuestionsString, prod);
                questionService.updateProductQuestions(prod, questions);
                request.getSession().setAttribute("productId", prod.getProductId());
                //pass upload image task to another servlet

                //image upload, if exists

              //  if (request.getPart("image").getInputStream().available()>0) {
                    ServletContext sc=getServletContext();
                    RequestDispatcher r=sc.getRequestDispatcher("/UploadImage");
                    r.forward(request, response);
               // }
               // else {
                   // response.setStatus(HttpServletResponse.SC_OK);
               // }


            }

            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain");
                response.getWriter().println("Please remove duplicated entries.");
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().println(checkNameDescription(productName, description));
        }

    }

    protected boolean checkNoDuplicateQuestions(List<String> questions) {
        HashSet<String> testSet = new LinkedHashSet<>();

        for (String name : questions) {
            if (testSet.add(name) == false) {
                return false;
            }
        }
        return true;

    }

    protected boolean checkQuestionsLength(List<String> questions) {

        for (String question : questions) {
            if (question.length()<2) return false;
        }
        return true;
    }

    private  Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    protected boolean isBeforeToday(Date date) {
        Date currentDate = getStartOfDay(new Date());

        return date.before(currentDate);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
