package it.polimi.db2.servlets;

import it.polimi.db2.auxiliary.QuestionnaireContent;
import it.polimi.db2.entities.Product;
import it.polimi.db2.services.ProductService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/HomepageData")
public class QuestionnairePageData extends HttpServlet {


    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        boolean exists = false;
        Product product;

        product = productService.getProductOfTheDay();
        exists = product != null;

        if (exists) {
            response.setStatus(HttpServletResponse.SC_OK);
            QuestionnaireContent pageData = new QuestionnaireContent(
                    product.getName(),
                    product.getDescription(),
                    ProductService.getQuestions(product));
            out.print(pageData.toJson());
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }



    }
}
