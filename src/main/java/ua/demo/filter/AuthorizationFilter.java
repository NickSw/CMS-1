package ua.demo.filter;

import ua.demo.entity.User;
import ua.demo.util.AuthorizationManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Sergey on 07.06.2015.
 */
public class AuthorizationFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest)servletRequest;
        //obtain session
        HttpSession session=req.getSession(false);
        if (session==null) {
            //session does not exist
            ((HttpServletResponse)servletResponse).sendRedirect("/login");
            return;
        }


        //get user stored in session
        User currentUser=(User)session.getAttribute("curuser");
        if (currentUser==null) {
            //user does not exist
            ((HttpServletResponse)servletResponse).sendRedirect("/login");
            return;

        } else {
            //user exist
            //get URI
            String URI=req.getRequestURI();

            //obtain AuthorizationManager singleton
            AuthorizationManager manager= AuthorizationManager.getInstance();

            boolean authorized=manager.isUserAuthorized(currentUser,URI);
            if (authorized) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                //access denied
                String[] head=new String[2];
                head[0]="Access denied: "+currentUser.getLogin();
                head[1]= currentUser.getRole()+" has no access.";
                req.setAttribute("head",head);


                req.getRequestDispatcher("/view/message.jsp").forward(servletRequest,servletResponse);
            }
        }
    }

    public void destroy() {

    }
}
