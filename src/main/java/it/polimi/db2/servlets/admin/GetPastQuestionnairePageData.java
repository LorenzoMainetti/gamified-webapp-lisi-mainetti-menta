package it.polimi.db2.servlets.admin;

import com.google.gson.Gson;
import it.polimi.db2.auxiliary.json.admin.DataEntry;
import it.polimi.db2.auxiliary.json.admin.PastQuestionnairePageContent;
import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.Product;
import it.polimi.db2.services.AdminService;
import it.polimi.db2.services.ProductService;
import jakarta.ejb.EJB;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/GetPastQuestionnairePageData")
public class GetPastQuestionnairePageData extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2.entities.services/AdminService")
    private AdminService adminService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //send all the past questionnaire ids, names and dates to the admin
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        //get current date and logged admin
        Date date = Date.valueOf(LocalDate.now());
        String adminId = (String) request.getSession().getAttribute("admin");
        Admin admin;
        try {
            admin = adminService.getAdmin(adminId);

            List<Product> pastQuestionnaires = productService.getPastQuestionnaires(date, admin);
            PastQuestionnairePageContent pqpc = new PastQuestionnairePageContent();

            for(Product product : pastQuestionnaires) {
                pqpc.getPastQuestionnaires().add(new DataEntry(product.getProductId(), product.getName(), product.getDate()));
            }
            String jsonResponse = new Gson().toJson(pqpc);
            out.print(jsonResponse);
        }
        catch (PersistenceException | InvalidParameterException e){
            sendError(request, response, "Admin Error", e.getCause().getMessage());
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
