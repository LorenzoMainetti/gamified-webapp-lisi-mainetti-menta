package it.polimi.db2.servlets.admin;

import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Question;
import it.polimi.db2.services.AdminService;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.QuestionService;
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

    /**
     * Method to get the date in the correct format
     * @param param date to parse
     * @return date parsed
     */
    private LocalDate getDateFromRequestParameter(String param) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(param, formatter);
    }

    /**
     * Method to convert a LocalDate to a Date
     * @param localDate date to convert
     * @return date converted
     */
    private Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Method that checks if the name and the description are correct
     * @param name name of the product tp check
     * @param description description of the product to check
     * @return control string to express the error
     */
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
     * @param request contains all parameters from Create Questionnaire form
     * @param response response
     * @author Ale Lisi & Main
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productName = StringEscapeUtils.escapeJava(request.getParameterValues("name")[0]);
        String description = StringEscapeUtils.escapeJava(request.getParameterValues("description")[0]);

        if (checkNameDescription(productName, description).equals("GO_FLIGHT")) {
            String sDate = request.getParameterValues("date")[0];
            Date date;
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
                ServletContext sc=getServletContext();
                RequestDispatcher r=sc.getRequestDispatcher("/UploadImage");
                r.forward(request, response);
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain");
                response.getWriter().println("Please remove duplicated entries.");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain");
            response.getWriter().println(checkNameDescription(productName, description));
        }

    }

    /**
     * Method that checks if there are no duplicate questions
     * @param questions list of questions
     * @return true if the list has no duplicates, false otherwise
     */
    protected boolean checkNoDuplicateQuestions(List<String> questions) {
        HashSet<String> testSet = new LinkedHashSet<>();
        for (String name : questions) {
            if (!testSet.add(name)) {
                return false;
            }
        }
        return true;

    }

    /**
     * Method that check the length of the questions
     * @param questions list of questions
     * @return true if the question have correct length, false otherwise
     */
    protected boolean checkQuestionsLength(List<String> questions) {
        for (String question : questions) {
            if (question.length()<2) return false;
        }
        return true;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
