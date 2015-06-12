package ua.demo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * filter closes access to view directory from out
 *
 * Created by Sergey on 09.06.2015.
 */
public class ViewFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp=(HttpServletResponse)servletResponse;
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }

    public void destroy() {

    }
}
