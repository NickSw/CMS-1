package ua.demo.servlet.admin;

import ua.demo.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Sergey on 02.06.2015.
 */
public class AdminServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User curUser=(User)req.getSession(false).getAttribute("curuser");
        req.setAttribute("curuser",curUser);

        String[] head=new String[2];
        head[0]="Welcome "+curUser.getLogin()+" to admin panel";
        head[1]="";
        req.setAttribute("head",head);


        req.getRequestDispatcher("/view/message.jsp").forward(req,resp);
    }
}
