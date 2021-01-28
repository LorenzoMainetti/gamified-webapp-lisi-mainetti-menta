package it.polimi.db2.servlets;

import com.google.gson.Gson;
import it.polimi.db2.auxiliary.json.LeaderboardContent;
import it.polimi.db2.entities.Product;
import it.polimi.db2.entities.Reward;
import it.polimi.db2.services.ProductService;
import it.polimi.db2.services.RewardService;
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

    protected void sendError(HttpServletRequest request, HttpServletResponse response, String errorType, String errorInfo) throws IOException {
        request.getSession().setAttribute ("errorType", errorType);
        request.getSession().setAttribute ("errorInfo", errorInfo);
        try {
            getServletConfig().getServletContext().getRequestDispatcher("/error.html").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Product prodOfTheDay = null;
        try {
            prodOfTheDay = productService.getProductOfTheDay();
            List<Reward> leaderboard = rewardService.getLeaderboard(prodOfTheDay);
            LeaderboardContent leaderboardContent = new LeaderboardContent(leaderboard);
            String jsonLeaderboard = new Gson().toJson(leaderboardContent);
            out.write(jsonLeaderboard);
        }catch (InvalidParameterException | EJBException e){
            System.out.println(e.getMessage());
            if(e.getCause().getMessage().equals("No product of the Day")){
                LeaderboardContent leaderboardContent = new LeaderboardContent(null);
                String jsonLeaderboard = new Gson().toJson(leaderboardContent);
                out.write(jsonLeaderboard);
                return;
            }
            else{
                sendError(request, response, "Database Error", e.getMessage());
                return;
            }
        }
    }
}
