package ua.demo.servlet.admin;

import ua.demo.dao.UserDAO;
import ua.demo.dao.impl.UserDAOImpl;
import ua.demo.entity.User;
import ua.demo.util.ConnectionFactory;
import ua.demo.util.ConnectionFactoryFactory;
import ua.demo.util.Hashing;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * servlet obtains "id" parameter from query string, create new user if `id` is empty or cannot be parsed
 * or update existing user if `id` > 0 in DB.
 *
 * Created by Sergey on 03.06.2015.
 */
public class UserUpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get current user from session
        User curUser=(User)req.getSession(false).getAttribute("curuser");
        req.setAttribute("curuser",curUser);

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
        String password=null;

        if ((firstName==null)||(firstName.isEmpty())) firstName=" ";
        if ((lastName==null)||(lastName.isEmpty())) lastName=" ";
        if ((login==null)||(login.isEmpty())) {
            error=true;
            errorMsg+=" empty login;";
        }
        int atPos=-1;
        if ((email==null)||(email.isEmpty())||((atPos=email.indexOf('@'))<1)||(atPos==email.length()-1)||(email.indexOf('@',atPos+1)!=-1)) {
            error=true;
            errorMsg+=" incorrect email;";
        }
        if ((pass1==null)||(pass1.isEmpty())||(pass2==null)||(pass2.isEmpty())||(!pass1.equals(pass2))) {
            error=true;
            errorMsg+=" empty password;";
        } else {
            password= Hashing.getHash(pass1);
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
            MessageSender.sendMessage("incorrect information:",errorMsg,req,resp);
            return;
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
            user.setPassword(password);
            user.setRoleId(roleId);

            //add or update user
            UserDAO userDAO=new UserDAOImpl(con);
            boolean res=userDAO.addUser(user);

            //close connection
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (res) {
                //ok user was added or updated
                MessageSender.sendMessage("Ok:", "", req, resp);
                return;
            }else {
                //error
                MessageSender.sendMessage("ERROR:", "", req, resp);
                return;
            }
        }
    }
}
