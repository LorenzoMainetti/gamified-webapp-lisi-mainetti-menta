package it.polimi.db2.servlets;

import com.google.gson.Gson;
import it.polimi.db2.auxiliary.json.HomepageContent;
import it.polimi.db2.auxiliary.UserStatus;
import it.polimi.db2.entities.Product;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.ReviewService;
import it.polimi.db2.services.UserService;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Base64;


@WebServlet("/HomepageData")
public class HomepageData extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/UserService")
    private UserService userService;
    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;
    @EJB(name = "it.polimi.db2.entities.services/ReviewService")
    private ReviewService reviewService;

    protected void sendError(HttpServletRequest request, HttpServletResponse response, String errorType, String errorInfo) throws IOException {
        request.getSession().setAttribute ("errorType", errorType);
        request.getSession().setAttribute ("errorInfo", errorInfo);
        try {
            getServletConfig().getServletContext().getRequestDispatcher("/error.html").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String username = (String) request.getSession().getAttribute("user");

        Product prodOfTheDay = null;
        try {
            prodOfTheDay = productService.getProductOfTheDay();
        }
        catch (InvalidParameterException | EJBException e){
            System.out.println(e.getMessage());
            if(e.getCause().getMessage().equals("No product of the Day")){
                HomepageContent homepageContent = new HomepageContent(username, false,
                        null, null, null, null,
                        null, UserStatus.NOT_AVAILABLE);
                String jsonHomepage = new Gson().toJson(homepageContent);
                out.write(jsonHomepage);
                return;
            }
            else{
                sendError(request, response, "Database Error", e.getMessage());
                return;
            }
        }
        UserStatus userStatus = userService.checkUserStatus(userService.getUser(username), prodOfTheDay, productService);
        ArrayList<String> reviews = null;
        try {
            reviews = reviewService.getRandomReviews();
        }
        catch (InvalidParameterException | EJBException e){
            System.out.println(e.getMessage());
        }
        byte[] image;
        String encoded = null;

        if (prodOfTheDay.getImage()!= null) encoded = Base64.getEncoder().encodeToString(prodOfTheDay.getImage());

        image = prodOfTheDay.getImage();


        HomepageContent homepageContent = new HomepageContent(username, false,
                prodOfTheDay.getName(), prodOfTheDay.getDescription(), image,
                encoded, reviews, userStatus);
        String jsonHomepage = new Gson().toJson(homepageContent);
        out.write(jsonHomepage);
    }
}
