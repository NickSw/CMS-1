package ua.demo.servlet.admin;

import ua.demo.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet generates welcome message to admin panel
 *
 * Created by Sergey on 02.06.2015.
 */
public class AdminServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get current user from session
        User curUser=(User)req.getSession(false).getAttribute("curuser");
        req.setAttribute("curuser",curUser);

        MessageSender.sendMessage("Welcome "+curUser.getLogin()+" to admin panel", "", req, resp);
        return;

    }
}
