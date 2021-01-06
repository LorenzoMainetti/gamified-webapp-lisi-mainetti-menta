package it.polimi.db2.servlets.admin;

import com.google.gson.Gson;
import it.polimi.db2.admin.AdminPageContent;
import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.Product;
import it.polimi.db2.exception.ProductNotFoundException;
import it.polimi.db2.services.AdminService;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.ReviewService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;

@WebServlet("/GetAdminPageData")
public class GetAdminPageData extends HttpServlet {

    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2.entities.services/ReviewService")
    private ReviewService reviewService;
    @EJB(name = "it.polimi.db2.entities.services/AdminService")
    private AdminService adminService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = setJsonResponse(response);

        AdminPageContent content = new AdminPageContent();

        Product podt;

        podt = productService.getProductOfTheDay();
         /* catch (ProductNotFoundException e) {
            //send error -- MERGE WITH ERROR IS NEEDED
            return;
        } */

        String adminId = (String) request.getSession().getAttribute("user");
        Admin admax = adminService.getAdmin(adminId);


        String encodedImage = Base64.getEncoder().encodeToString(podt.getImage());


        content.setProdDescription(podt.getDescription());
        content.setProdName(podt.getName());
        content.setEmail(admax.getEmail());
        content.setAdminId(admax.getAdminId());
        content.setEncodedImg(encodedImage);

        LinkedHashMap<Date, String> listOfPastQuest = new LinkedHashMap<>();


        out.print(new Gson().toJson(content));



    }

    private PrintWriter setJsonResponse(HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        return out;
    }

}
