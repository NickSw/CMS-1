package ua.demo.servlet;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * servlet receives URI for needed image, gets image file name,
 * gets this file from directory where actually images are stored
 * and sends image through response.
 * Thereby all images available at URL= /images/*  in spite of actually images are stored in different directory.
 *
 *
 * Created by Sergey on 09.06.2015.
 */
public class LoadImageServlet extends HttpServlet {
    private final int BUFFER_LENGTH=4096;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filePath = req.getRequestURI();

        String directoryPath=null;
        if ((directoryPath=System.getenv("OPENSHIFT_DATA_DIR"))==null) {
            directoryPath = getServletContext().getRealPath("/images/")+"/";
        }

        File file = new File(directoryPath + filePath.replace("/images/", ""));
        InputStream input = new FileInputStream(file);

        resp.setContentLength((int) file.length());
        resp.setContentType(new MimetypesFileTypeMap().getContentType(file));

        OutputStream output = resp.getOutputStream();
        byte[] bytes = new byte[BUFFER_LENGTH];
        int read = 0;
        while ((read = input.read(bytes, 0, BUFFER_LENGTH)) != -1) {
            output.write(bytes, 0, read);
            output.flush();
        }

        input.close();
        output.close();
    }

}

