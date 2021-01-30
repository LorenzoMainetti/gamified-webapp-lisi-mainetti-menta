package it.polimi.db2.servlets.admin;

import com.google.gson.Gson;
import it.polimi.db2.admin.InspectionPageContent;
import it.polimi.db2.entities.Answer;
import it.polimi.db2.entities.Product;
import it.polimi.db2.services.ProductService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/GetInspectionData")
public class GetInspectionData extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    /**
     * Method to check if the productId is valid
     * @param productId id of the product to check
     * @return true if it's correct, false otherwise
     */
    boolean checkProductId (int productId) {
        return productId > 0;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = (Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("pid"))));

        if(checkProductId(productId)) {
            try {
                List<String> usersWhoSubmitted, usersWhoCanceled;
                Map<String, List<String>> answersForEachUser = new HashMap<>();

                usersWhoSubmitted = new LinkedList<>();
                usersWhoCanceled = new LinkedList<>();

                Product product = productService.getProduct(productId);
                productService.getProductUsers(product, false).forEach( u -> {
                    usersWhoCanceled.add(u.getUsername());
                });
                productService.getProductUsers(product, true).forEach( u -> {
                    usersWhoSubmitted.add(u.getUsername());
                });

                List<String> questions = product.getQuestionsText();

                for (String s : usersWhoSubmitted) {
                    List <Answer> answersFromUser = productService.getUserAnswers(product, s);
                    answersForEachUser.put(s, productService.getOrderedAnswers(product.getQuestions(), answersFromUser));
                }
                String encoded = null;

                if (product.getImage()!= null) encoded = Base64.getEncoder().encodeToString(product.getImage());

                InspectionPageContent content;
                content = new InspectionPageContent(usersWhoSubmitted, usersWhoCanceled, answersForEachUser, questions,
                        product.getName(), product.getDescription(), encoded, product.getDate());

                String jsonResponse = new Gson().toJson(content);

                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);

                out.write(jsonResponse);
            } catch (Exception e) {
                sendError(request, response, "Inspection Error", e.getCause().getMessage());
            }
        }
    }

    /**
     * Method to handle errors, redirects to an error page
     * @param request request
     * @param response response
     * @param errorType type of error
     * @param errorInfo information about the error
     * @throws IOException if there are problems redirecting
     */
    protected void sendError(HttpServletRequest request, HttpServletResponse response, String errorType, String errorInfo) throws IOException {
        request.getSession().setAttribute ("errorType", errorType);
        request.getSession().setAttribute ("errorInfo", errorInfo);
        try {
            getServletConfig().getServletContext().getRequestDispatcher("/error.html").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
