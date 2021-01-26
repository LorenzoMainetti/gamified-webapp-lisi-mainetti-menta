package it.polimi.db2.servlets.admin;

import it.polimi.db2.entities.Admin;
import it.polimi.db2.entities.Product;
import it.polimi.db2.services.*;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;


@WebServlet("/DeleteQuestionnaire")
@MultipartConfig
public class DeleteQuestionnaire extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2.entities.services/RewardService")
    private RewardService rewardService;
    @EJB(name = "it.polimi.db2.entities.services/QuestionService")
    private QuestionService questionService;
    @EJB(name = "it.polimi.db2.entities.services/AdminService")
    private AdminService adminService;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the id of the product to be deleted
        int productId = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("prodId")));
        Product product = productService.getProduct(productId);

        String adminId = (String) request.getSession().getAttribute("admin");
        Admin admin = adminService.getAdmin(adminId);

        rewardService.deleteQuestionnaire(product); //TODO check if really needed

        //TODO delete all answers associated with the product to be deleted

        //delete the product and delete it also from the users' list and the admin list
        productService.deleteProduct(product);
        admin.removeCreatedProduct(product);

        response.sendRedirect("past.html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

