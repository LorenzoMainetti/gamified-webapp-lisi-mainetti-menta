package it.polimi.db2.servlets;

import com.google.gson.Gson;
import it.polimi.db2.auxiliary.HomepageContent;
import it.polimi.db2.auxiliary.LeaderboardContent;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Reward;
import it.polimi.db2.entities.User;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.RewardService;
import jakarta.ejb.EJB;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Leaderboard")
public class Leaderboard extends HttpServlet {

    @EJB(name = "it.polimi.db2.entities.services/RewardService")
    private RewardService rewardService;
    @EJB(name = "it.polimi.db2.entities.services/ProductService")
    private ProductService productService;

    public Leaderboard() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        //String username = (String) request.getSession().getAttribute("user");
        Product productToday = null;
        try {
            productToday = productService.getProductOfTheDay();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        List<Reward> leaderboard = null;
        try {
             leaderboard = rewardService.getLeaderboard(productToday);
        } catch (PersistenceException | InvalidParameterException e) {
            e.printStackTrace();
        }
        //TODO HARDCODED
        LeaderboardContent leaderboardContent = new LeaderboardContent(leaderboard);
        String jsonLeaderboard = new Gson().toJson(leaderboardContent);
        out.write(jsonLeaderboard);
    }
}
