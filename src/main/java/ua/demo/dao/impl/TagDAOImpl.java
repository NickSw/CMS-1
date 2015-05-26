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


}
