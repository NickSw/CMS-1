package ua.demo.servlet.admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * auxiliary servlet, which creates page with message
 *
 * Created by Sergey on 12.06.2015.
 */
public class MessageSender {
    public static void sendMessage (String header,String message,HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        String[] head=new String[2];
        head[0]=header;
        head[1]=message;
        req.setAttribute("head",head);

        req.getRequestDispatcher("/view/message.jsp").forward(req, resp);
    }
}
