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


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the id of the product to be deleted
        int productId = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("prodId")));
        Product product = productService.getProduct(productId);

        Admin admin = product.getCreator();

        //delete the product and delete it also from the users' list and the admin list
        productService.deleteProduct(product);
        admin.removeCreatedProduct(product);

        response.sendRedirect("Admin/past.html?");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

