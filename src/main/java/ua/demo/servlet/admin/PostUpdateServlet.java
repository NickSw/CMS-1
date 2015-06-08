package ua.demo.servlet.admin;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ua.demo.dao.PostAdminDAO;
import ua.demo.dao.impl.PostAdminDAOImpl;
import ua.demo.entity.Post;
import ua.demo.entity.User;
import ua.demo.util.ConnectionFactory;
import ua.demo.util.ConnectionFactoryFactory;
import ua.demo.util.ResizeImage;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sergey on 04.06.2015.
 */
public class PostUpdateServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User curUser=(User)req.getSession(false).getAttribute("curuser");
        req.setAttribute("curuser",curUser);

        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (isMultipart){
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Configure a repository (to ensure a secure temp location is used)
            ServletContext servletContext = this.getServletConfig().getServletContext();
            File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            //create object Post
            Post post=new Post();
            String path="";

            // Parse the request
            try {
                List<FileItem> items = upload.parseRequest(req);

                // Process the uploaded items - form fields
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();

                    if (item.isFormField()) {
                        String errorMsg=processFormField(item,post,curUser);
                        if (errorMsg!=null)
                        {
                            String[] head=new String[2];
                            head[0]="Incorrect information:";
                            head[1]=errorMsg;
                            req.setAttribute("head",head);

                            req.getRequestDispatcher("/view/message.jsp").forward(req, resp);
                        }
                    }
                }


            /* update information in DB*/

            //creates connection
            ConnectionFactory conf = ConnectionFactoryFactory.getConnectionFactory();
            Connection con = conf.getConnection();

            //update information in DB
            PostAdminDAO postAdminDAO=new PostAdminDAOImpl(con);

                int id=-1;
                if (post.getId()<1) {
                //add new post

                //returns id for new post
                // if id=-1 something wrong
                id = postAdminDAO.addPost(post);
            } else {
                //update existing post
                id=postAdminDAO.updatePost(post);
                }


            //close connection
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            if (id>0) {
                //post has been added or updated
                //upload file
                iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    if (!item.isFormField()){
                        String fileNameLarge="large_"+id+".jpg";
                        String fileNameSmall="small_"+id+".jpg";

                        path = processUploadedFile(item,fileNameLarge,fileNameSmall);
                    }
                }
                    if (path==null)
                {
//                    String[] head=new String[2];
//                    head[0]="Warranty:";
//                    head[1]="post has been added to DB, but file was not uploaded";
//
//                    req.setAttribute("head",head);
//
//                    req.getRequestDispatcher("/view/message.jsp").forward(req, resp);
                }


            } else {
                //post has not been added
                String[] head=new String[2];
                head[0]="Error:";
                head[1]="post has not been added to DB";
                req.setAttribute("head",head);

                req.getRequestDispatcher("/view/message.jsp").forward(req, resp);
            }

            } catch (FileUploadException e) {
                e.printStackTrace();

                String[] head=new String[2];
                head[0]="Error:";
                head[1]="";
                req.setAttribute("head",head);

                req.getRequestDispatcher("/view/message.jsp").forward(req, resp);
            }
            //all right, post was added and image was upload
            String[] head=new String[2];
            head[0]="OK:";
            head[1]="post was added";
            req.setAttribute("head",head);

            req.getRequestDispatcher("/view/message.jsp").forward(req, resp);

      }
    }

    // Process a file upload
    //return path to uploaded file, if something wrong return null
    private String processUploadedFile(FileItem item,String fileNameLarge,String fileNameSmall){

            //check if it is jpeg
        if (!item.getContentType().equals("image/jpeg")){
            return null;
        }
            long sizeInBytes = item.getSize();

            // Process a file upload
        try {
            String path = getServletContext().getRealPath("/images/" +fileNameLarge);
            File uploadedFile = new File(path);

            uploadedFile.createNewFile();
            item.write(uploadedFile);

            //fit image to width
            ResizeImage ri=new ResizeImage();
            ri.fitWidth(path);
            //creates small copy
            String resPath = getServletContext().getRealPath("/images/" +fileNameSmall);
            ri.resizeAndCrop(path,resPath);


            return path;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    //checks and fill information to object Post
    //if somthing wrong return error message otherwise return null
    private String processFormField(FileItem item,Post post,User curUser){
        // Process a regular form field

        String name = item.getFieldName();
        String value = item.getString();

        //check parameters
        String errorMsg="";
        boolean error=false;

        if (name.equals("id")){
            int id=-1;
            try {
                id=Integer.parseInt(value);
            } catch (Exception ex) {}
            post.setId(id);
        }

        if (name.equals("title")){
            if ((value==null)||(value.isEmpty())){
                error=true;
                errorMsg="empty title";
            } else {
                value=value.replace("'","`");
                post.setTitle(value);
            }
        }

        if (name.equals("date")){
            Date date=new Date();
            try {
                SimpleDateFormat dateLong=new SimpleDateFormat("yyyy-MM-dd");
                date=dateLong.parse(value);

            } catch (ParseException e) {}
            post.setCreationDate(date);
        }

        if (name.equals("ordering")){
            int ordering=0;
            try {
                ordering=Integer.parseInt(value);
            } catch (Exception ex){

            }
            post.setOrdering(ordering);
        }

        if (name.equals("feature")) {
            boolean mark;
            if ((value==null)||(value.isEmpty())) mark=false;
            else mark=true;

            post.setMark(mark);
        }

        if (name.equals("tag1")) {
            int tag1=-1;
            try {
                tag1=Integer.parseInt(value);
            } catch (Exception ex){

            }
            post.setTagId1(tag1);
        }

        if (name.equals("tag2")){
            if (name.equals("tag2")) {
                int tag2=-1;
                try {
                    tag2=Integer.parseInt(value);
                } catch (Exception ex){

                }
                post.setTagId2(tag2);
            }
        }
        if (name.equals("tag3")){
            if (name.equals("tag3")) {
                int tag3=-1;
                try {
                    tag3=Integer.parseInt(value);
                } catch (Exception ex){

                }
                post.setTagId3(tag3);
            }
        }

        if (name.equals("content")){
            if ((value==null)||(value.isEmpty())){
                error=true;
                errorMsg="empty content";
            } else {
                value=value.replace("'","`");
                post.setContent(value);
            }
        }

        if (name.equals("userid")){
            int userId=curUser.getId();
            try {
                userId=Integer.parseInt(value);
            } catch (Exception ex) {}
            post.setUserId(userId);
        }

        if (error) return errorMsg;
        else return null;

    }
}
