package ua.demo.servlet;

import ua.demo.dao.RoleDAO;
import ua.demo.dao.UserDAO;
import ua.demo.dao.impl.RoleDAOImpl;
import ua.demo.dao.impl.UserDAOImpl;
import ua.demo.entity.Role;
import ua.demo.entity.User;
import ua.demo.util.ConnectionFactory;
import ua.demo.util.ConnectionFactoryFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Sergey on 03.06.2015.
 */
public class UserUpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get query string parameters
        String idStr=req.getParameter("id");
        String firstName=req.getParameter("first");
        String lastName=req.getParameter("last");
        String login=req.getParameter("login");
        String email=req.getParameter("email");
        String pass1=req.getParameter("pass1");
        String pass2=req.getParameter("pass2");
        String roleStr=req.getParameter("role");

        //check paramameters
        boolean error=false;
        String errorMsg="";

        if ((firstName==null)||(firstName.isEmpty())) firstName=" ";
        if ((lastName==null)||(lastName.isEmpty())) lastName=" ";
        if ((login==null)||(login.isEmpty())) {
            error=true;
            errorMsg+=" login;";
        }
        if ((email==null)||(email.isEmpty())||(email.indexOf('@')<0)) {
            error=true;
            errorMsg+=" email;";
        }
        if ((pass1==null)||(pass1.isEmpty())||(pass2==null)||(pass2.isEmpty())||(!pass1.equals(pass2))) {
            error=true;
            errorMsg+=" password;";
        }

        int roleId=-1;
        try {
            roleId = Integer.parseInt(roleStr);
        } catch (Exception ex) {
            error=true;
            errorMsg+=" role;";
        }

        int id=-1;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception ex) {}

        if (error){
            String[] head=new String[2];
            head[0]="incorrect information:";
            head[1]=errorMsg;
            req.setAttribute("head",head);

            req.getRequestDispatcher("/view/message.jsp").forward(req, resp);
        }
        else {
            //create connection
            ConnectionFactory conf= ConnectionFactoryFactory.getConnectionFactory();
            Connection con=conf.getConnection();

            //create user
            User user=new User();
            user.setId(id);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setLogin(login);
            user.setEmail(email);
            user.setPassword(pass1);
            user.setRoleId(roleId);

            //add user
            UserDAO userDAO=new UserDAOImpl(con);
            boolean res=userDAO.addUser(user);

            //close connection
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (res) {
                resp.sendRedirect("/admin/users");
                return;
            }else {

                String[] head=new String[2];
                head[0]="ERROR:";
                head[1]="";
                req.setAttribute("head",head);

                req.getRequestDispatcher("/view/message.jsp").forward(req, resp);
            }
        }
    }
}
