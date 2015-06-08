package ua.demo.dao.impl;

import ua.demo.dao.TagDAO;
import ua.demo.entity.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDAOImpl implements TagDAO {
    private final String SELECT_ALL="SELECT * FROM tag";
    private final String SELECT_BY_POST="SELECT id,tag_name FROM tag,(SELECT * FROM post_tag WHERE post_id=?) AS post_tagg WHERE tag_id=tag.id";
    private final String SELECT_BY_ID="SELECT * FROM tag WHERE id=?";
    private final String INSERT_TAG="INSERT INTO tag (tag_name) VALUES ('${tagname}')";
    private final String UPDATE_TAG_BY_ID="UPDATE tag SET tag_name='${tagname}' WHERE id=${id}";
    private final String DELETE_BY_ID="DELETE FROM tag WHERE id=";
    private final String DELETE_FROM_TAG_POST_BY_TAG="DELETE FROM post_tag WHERE tag_id=";


    private Connection con;
    public TagDAOImpl(Connection connection) {
        con=connection;
    }

    public Tag getById(int id) {
        try {
            PreparedStatement pstat = con.prepareStatement(SELECT_BY_ID);
            pstat.setInt(1,id);
            ResultSet rs=pstat.executeQuery();
            Tag tag=new Tag();
            while(rs.next())
            {
                tag.setId(rs.getInt("id"));
                tag.setTagName(rs.getString("tag_name"));
            }
            if (tag.getId()==0) return null;
            else return tag;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Tag> getAll() {
        try {
            Statement stat = con.createStatement();
            ResultSet rs=stat.executeQuery(SELECT_ALL);
            List<Tag> tags=new ArrayList<Tag>();
            while(rs.next())
            {
                Tag tag=new Tag();
                tag.setId(rs.getInt("id"));
                tag.setTagName(rs.getString("tag_name"));

                tags.add(tag);
            }
            return tags;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<Tag> getByPost(int id) {
        try {
            PreparedStatement pstat = con.prepareStatement(SELECT_BY_POST);
            pstat.setInt(1,id);
            ResultSet rs=pstat.executeQuery();
            List<Tag> tags=new ArrayList<Tag>();
            while(rs.next())
            {
                Tag tag=new Tag();
                tag.setId(rs.getInt("id"));
                tag.setTagName(rs.getString("tag_name"));

                tags.add(tag);
            }
            return tags;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addTag(Tag tag) {
        int res=-1;
        boolean error=false;
        if (tag.getId() < 1) {
            // add new tag
            try {
                Statement stat = con.createStatement();
                String query = INSERT_TAG;
                query=query.replace("${tagname}", tag.getTagName());

                res = stat.executeUpdate(query);

            } catch (SQLException e) {
                error=true;
                e.printStackTrace();
            }
        }
        else {
            //update tag
            try {
                Statement stat = con.createStatement();
                String query = UPDATE_TAG_BY_ID;
                query=query.replace("${tagname}", tag.getTagName());
                query=query.replace("${id}", String.valueOf(tag.getId()));

                res = stat.executeUpdate(query);

            } catch (SQLException e) {
                error=true;
                e.printStackTrace();
            }
        }

        if ((res>0)&&(!error)) return true;
        else return false;
    }


        public boolean deleteById(int id) {
            int res=-1;
            try {
                con.setAutoCommit(false);

                Statement stat=con.createStatement();
                //delete from post_tag
                stat.executeUpdate(DELETE_FROM_TAG_POST_BY_TAG+id);
                //delete from tag
                res=stat.executeUpdate(DELETE_BY_ID+id);

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

            if (res<1) return false;
            else return true;

        }



}
