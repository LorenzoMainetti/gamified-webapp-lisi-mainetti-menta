package it.polimi.db2.servlets.admin;

import com.google.gson.Gson;
import it.polimi.db2.admin.PastQuestionnairePageContent;
import it.polimi.db2.entities.Product;
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
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@WebServlet("/GetPastQuestionnairePageData")
public class GetPastQuestionnairePageData extends HttpServlet {

    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //send all the questionnaire ids and other data to the admin

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PastQuestionnairePageContent content= new PastQuestionnairePageContent();

        //get current date
        Date date = Date.valueOf(LocalDate.now());
        List<Product> pastQuestionnaires = productService.getPastQuestionnaires(date);

        String encodedImage;

        for(Product product : pastQuestionnaires) {
            content.setProdId(product.getProductId());
            content.setProdName(product.getName());
            content.setProdDate(product.getDate());
            content.setProdDescription(product.getDescription());
            encodedImage = Base64.getEncoder().encodeToString(product.getImage());
            content.setEncodedImg(encodedImage);
        }

        out.print(new Gson().toJson(content));

    }

}
