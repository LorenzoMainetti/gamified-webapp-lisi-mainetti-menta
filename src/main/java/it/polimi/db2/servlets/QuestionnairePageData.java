package it.polimi.db2.servlets;

import it.polimi.db2.auxiliary.QuestionnaireContent;
import it.polimi.db2.entities.Product;
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


@WebServlet("/QuestionnairePageData")
public class QuestionnairePageData extends HttpServlet {
    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;

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

        Product prodOfTheDay = null;
        try {
            prodOfTheDay = productService.getProductOfTheDay();
            response.setStatus(HttpServletResponse.SC_OK);
            QuestionnaireContent pageData = new QuestionnaireContent(prodOfTheDay.getName(),
                    prodOfTheDay.getDescription(), ProductService.getQuestions(prodOfTheDay));
            out.print(pageData.toJson());
        }
        catch (InvalidParameterException | EJBException e){
            sendError(request, response, "Database Error", e.getMessage());
        }
    }
}
