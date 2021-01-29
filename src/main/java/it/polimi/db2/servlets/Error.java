package it.polimi.db2.servlets;

import com.google.gson.Gson;
import it.polimi.db2.auxiliary.json.ErrorContent;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Error")
public class Error extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String errorType = (String) request.getSession().getAttribute("errorType");
        request.getSession().removeAttribute("errorType");
        String errorInfo = (String) request.getSession().getAttribute("errorInfo");
        request.getSession().removeAttribute("errorInfo");
        ErrorContent errorContent = new ErrorContent(errorType, errorInfo);
        String jsonError = new Gson().toJson(errorContent);
        out.print(jsonError);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
