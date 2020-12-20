package it.polimi.db2.servlets;

import com.google.gson.Gson;
import it.polimi.db2.auxiliary.HomepageContent;
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
import java.text.ParseException;


@WebServlet("/HomepageData")
public class HomepageData extends HttpServlet {


    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;

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
        HomepageContent gg = new HomepageContent(username, false, podt.getName(), "prova descrizione", null);
        String jsonHomepage = new Gson().toJson(gg);
        out.print(jsonHomepage);

    }
}