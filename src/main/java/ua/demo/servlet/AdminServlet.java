package ua.demo.servlet;

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

        String[] head=new String[2];
        head[0]="Welcome:";
        head[1]="";
        req.setAttribute("head",head);

        req.getRequestDispatcher("/view/message.jsp").forward(req,resp);
    }
}
