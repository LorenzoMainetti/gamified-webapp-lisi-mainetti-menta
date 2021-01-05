package it.polimi.db2.servlets;

import com.google.gson.Gson;
import it.polimi.db2.auxiliary.HomepageContent;
import it.polimi.db2.auxiliary.images.ImageProcessor;
import it.polimi.db2.entities.Product;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.ReviewService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;


@WebServlet("/HomepageData")
public class HomepageData extends HttpServlet {

    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2.entities.services/ReviewService")
    private ReviewService reviewService;

    Product getProductOfTheDay() throws ParseException {
        return productService.getProductOfTheDay();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String username = (String) request.getSession().getAttribute("user");

        Product podt = null;
        try {
            podt = getProductOfTheDay();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //TODO HARDCODED
        String productImageURI = "#";
        String encoded = Base64.getEncoder().encodeToString(podt.getImage());
        HomepageContent gg = new HomepageContent(username, false, podt.getName(),
                podt.getDescription(), podt.getImage(), encoded, reviewService.getRandomReviews());
        String jsonHomepage = new Gson().toJson(gg);
        //out.print(jsonHomepage);
        out.write(jsonHomepage);
    }
}
