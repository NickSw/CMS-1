package ua.demo.servlet.admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * servlet log out user i.e.
 * removes user object from session and closes it
 *
 * Created by Sergey on 07.06.2015.
 */
public class LogoutServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession(false);
        if (session!=null) {
            session.removeAttribute("curuser");
            session.invalidate();
        }

        resp.sendRedirect("/");
        return;
    }
}
