package it.polimi.db2.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/LoggedIn")
public class LoggedIn implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Login checker filter executing ...");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String loginPath = req.getServletContext().getContextPath() + "./";

        HttpSession s = req.getSession();
        if (s.isNew() || s.getAttribute("user") == null) {
            System.out.println("Inconsistency found, redirecting ...");
            res.sendRedirect(loginPath);
        }
        else {
            // pass the request along the filter chain
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
