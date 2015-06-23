package ua.demo.dao.impl;

import ua.demo.dao.PostAdminDAO;
import ua.demo.entity.Post;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 04.06.2015.
 */
public class PostAdminDAOImpl implements PostAdminDAO{
    private final String SELECT_ALL="SELECT * FROM post,user WHERE user_id=user.id";
    private final String SELECT_COUNT_ALL="SELECT COUNT(*) FROM post";
    private final String INSERT_POST="INSERT INTO post (title,creation_date,last_update_date,content,ordering,mark,user_id) VALUES ('${title}','${date}','2015-01-01 00:00:00','${content}',${ordering},${mark},${userid})";
    private final String INSERT_POST_TAG="INSERT INTO post_tag VALUES (${postid},${tagid})";
    private final String DELETE_BY_ID="DELETE FROM post WHERE id=";
    private final String DELETE_BY_ID_FROM_POST_TAG="DELETE FROM post_tag WHERE post_id=";
    private final String SELECT_BY_ID="SELECT * FROM post WHERE id=";
    private final String SELECT_TAG_ID_BY_POSTID="SELECT * FROM post_tag WHERE post_id=";
    private final String UPDATE_POST_BY_ID="UPDATE post SET title='${title}',creation_date='${date}',last_update_date='2015-01-01 00:00:00',content='${content}',ordering=${ordering},mark=${mark},user_id=${userid} WHERE id=";
    private final String UDATE_POST_TAG="UPDATE post_tag SET post_id${postid},${tagid})";
    private Connection con;

    public PostAdminDAOImpl (Connection con){
        this.con=con;
    }




    public List<Post> getAll(int start, int limit,int order) {

        //creates query
        String query=SELECT_ALL;
        switch (order)
        {
            case 0: break;
            case 1: query+=" ORDER BY title"; break;
            case 2: query+=" ORDER BY creation_date DESC"; break;
            case 3: query+=" ORDER BY ordering DESC"; break;
            case 4: query+=" ORDER BY login"; break;

        }

        try {
            Statement pstat = con.createStatement();
            ResultSet rs=pstat.executeQuery(query + " LIMIT " + (start-1) + "," + limit);
            List<Post> posts = new ArrayList<Post>();
            while(rs.next())
            {
                Post post = new Post();
                fillValues(post,rs);
                post.setUserLogin(rs.getString("login"));
                posts.add(post);
            }
            return posts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getAmountOfAllPosts() {
        try {
            Statement stat = con.createStatement();
            ResultSet rs=stat.executeQuery(SELECT_COUNT_ALL);
            int count=-1;
            while(rs.next())
            {
                count=rs.getInt(1);
            }
            return count;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    //aupdate existing post to DB and returns its id,
    // else returns -10 for duplicate entry or -1 for unknown error
    public int updatePost(Post post) {
        int res=-1;

        String query=UPDATE_POST_BY_ID;
        query=query.replace("${title}",post.getTitle());
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        query=query.replace("${date}",df.format(post.getCreationDate()));
        query=query.replace("${content}",post.getContent());
        query=query.replace("${ordering}",String.valueOf(post.getOrdering()));
        query=query.replace("${mark}",String.valueOf(post.getMark()));
        query=query.replace("${userid}",String.valueOf(post.getUserId()));
        query=query+post.getId();

        Statement stat=null;
        try {

            con.setAutoCommit(false);
            //update post
            stat=con.createStatement();
            int count=stat.executeUpdate(query);

            if (count>0) {
                //update related tags=delete related tags and then insert related tags
                stat.executeUpdate(DELETE_BY_ID_FROM_POST_TAG + post.getId());
                if (post.getTagId1() > 0) {
                    query = INSERT_POST_TAG;
                    query = query.replace("${postid}", String.valueOf(post.getId()));
                    query = query.replace("${tagid}", String.valueOf(post.getTagId1()));

                    stat.executeUpdate(query);
                }

                if ((post.getTagId2() > 0)&&(post.getTagId2()!=post.getTagId1())) {
                    query = INSERT_POST_TAG;
                    query = query.replace("${postid}", String.valueOf(post.getId()));
                    query = query.replace("${tagid}", String.valueOf(post.getTagId2()));

                    stat.executeUpdate(query);
                }

                if ((post.getTagId3() > 0)&&(post.getTagId3()!=post.getTagId1())&&(post.getTagId3()!=post.getTagId2())) {
                    query = INSERT_POST_TAG;
                    query = query.replace("${postid}", String.valueOf(post.getId()));
                    query = query.replace("${tagid}", String.valueOf(post.getTagId3()));

                    stat.executeUpdate(query);
                }
            }
            con.commit();
            con.setAutoCommit(true);
            if (count>0)res=post.getId();
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) res=-10;
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return res;


    }

    //add post to DB and returns its id,
    // else returns -10 for duplicate entry or -1 for unknown error
    public int addPost(Post post) {
        int res=-1;

        String query=INSERT_POST;
        query=query.replace("${title}",post.getTitle());
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        query=query.replace("${date}",df.format(post.getCreationDate()));
        query=query.replace("${content}",post.getContent());
        query=query.replace("${ordering}",String.valueOf(post.getOrdering()));
        query=query.replace("${mark}",String.valueOf(post.getMark()));
        query=query.replace("${userid}",String.valueOf(post.getUserId()));

        Statement stat=null;
        try {

            con.setAutoCommit(false);
            //insert post
            stat=con.createStatement();
            int count=stat.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs=stat.getGeneratedKeys();
            while (rs.next())
            {
                //get autogenerated id of new post
                res=rs.getInt(1);
            }


                //insert related tags
                if (post.getTagId1() > 0) {
                    query = INSERT_POST_TAG;
                    query = query.replace("${postid}", String.valueOf(res));
                    query = query.replace("${tagid}", String.valueOf(post.getTagId1()));

                    stat.executeUpdate(query);
                }

                if ((post.getTagId2() > 0)&&(post.getTagId2()!=post.getTagId1())) {
                    query = INSERT_POST_TAG;
                    query = query.replace("${postid}", String.valueOf(res));
                    query = query.replace("${tagid}", String.valueOf(post.getTagId2()));

                    stat.executeUpdate(query);
                }

                if ((post.getTagId3() > 0)&&(post.getTagId3()!=post.getTagId1())&&(post.getTagId3()!=post.getTagId2())) {
                    query = INSERT_POST_TAG;
                    query = query.replace("${postid}", String.valueOf(res));
                    query = query.replace("${tagid}", String.valueOf(post.getTagId3()));

                    stat.executeUpdate(query);
                }

            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) res=-10;
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return res;
    }

    public boolean deleteById(int id) {
        int count=-1;
        try {
            con.setAutoCommit(false);
            Statement stat=con.createStatement();
            //delete post from post_tag
            stat.executeUpdate(DELETE_BY_ID_FROM_POST_TAG+id);

            //delete post from post table
            count=stat.executeUpdate(DELETE_BY_ID+id);



        con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (count>0) return true;
        else return false;
    }

    public Post getById(int id) {
        try {
            Statement stat=con.createStatement();
            ResultSet rs=stat.executeQuery(SELECT_BY_ID + id);
            Post post=new Post();
            while(rs.next()){
                fillValues(post,rs);
                post.setContent(rs.getString("content"));
            }

            rs=stat.executeQuery(SELECT_TAG_ID_BY_POSTID+id);
            int i=1;
            while(rs.next()){
                switch (i) {
                    case 1: post.setTagId1(rs.getInt("tag_id")); break;
                    case 2: post.setTagId2(rs.getInt("tag_id")); break;
                    case 3: post.setTagId3(rs.getInt("tag_id")); break;
                }
                i++;
            }
            return post;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //copy values from ResultSet to object Post
    private void fillValues(Post post,ResultSet rs) throws SQLException {
        post.setId(rs.getInt("id"));
        post.setTitle(rs.getString("title"));
        post.setCreationDate(rs.getTimestamp("creation_date"));
        post.setLastUpdateDate(rs.getTimestamp("last_update_date"));
        post.setOrdering(rs.getInt("ordering"));
        post.setMark(rs.getBoolean("mark"));
        post.setUserId(rs.getInt("user_id"));
    }
}
