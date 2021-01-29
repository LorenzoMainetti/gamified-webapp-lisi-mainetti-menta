package it.polimi.db2.servlets.admin;

import com.google.gson.Gson;
import it.polimi.db2.admin.AdminHomePageContent;
import it.polimi.db2.auxiliary.AdminStatus;
import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.Product;
import it.polimi.db2.services.AdminService;
import it.polimi.db2.services.ProductService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.Base64;


@WebServlet("/GetAdminHomePageData")
public class GetAdminHomePageData extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2.entities.services/AdminService")
    private AdminService adminService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = setJsonResponse(response);

        String adminId = (String) request.getSession().getAttribute("admin");
        Admin admin = adminService.getAdmin(adminId);

        AdminHomePageContent content;

        Product prodOfTheDay;
        try {
            prodOfTheDay = productService.getProductOfTheDay();
        }
        catch (InvalidParameterException | EJBException e){
            System.out.println(e.getMessage());
            if(e.getCause().getMessage().equals("No product of the Day")){
                content = new AdminHomePageContent(admin.getAdminId(),admin.getEmail(), null,
                        null, null, AdminStatus.NOT_AVAILABLE);
                out.write(new Gson().toJson(content));
                return;
            }
            else{
                sendError(request, response, "Database Error", e.getMessage());
                return;
            }
        }
        String encoded = null;
        if (prodOfTheDay.getImage()!= null) encoded = Base64.getEncoder().encodeToString(prodOfTheDay.getImage());

        content = new AdminHomePageContent(admin.getAdminId(),admin.getEmail(), prodOfTheDay.getName(),
                prodOfTheDay.getDescription(), encoded, AdminStatus.AVAILABLE);

        out.print(new Gson().toJson(content));
    }

    private PrintWriter setJsonResponse(HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        return out;
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
