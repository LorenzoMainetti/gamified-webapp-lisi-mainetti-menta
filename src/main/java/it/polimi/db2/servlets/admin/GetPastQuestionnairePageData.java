package it.polimi.db2.servlets.admin;

import com.google.gson.Gson;
import it.polimi.db2.admin.PastQuestionnairePageContent;
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
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/GetPastQuestionnairePageData")
public class GetPastQuestionnairePageData extends HttpServlet {

    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //send all the past questionnaire ids, names and dates to the admin

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        //get current date
        Date date = Date.valueOf(LocalDate.now());
        List<Product> pastQuestionnaires = productService.getPastQuestionnaires(date);

        PastQuestionnairePageContent pqpc = new PastQuestionnairePageContent();

        for(Product product : pastQuestionnaires) {
            pqpc.addEntry(product.getProductId(), product.getName(), product.getDate());
        }

        out.print(new Gson().toJson(pqpc.getJsonRepresentation()));

    }

}
