package ua.demo.dao.impl;

import ua.demo.dao.PostDAO;
import ua.demo.entity.Post;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class PostDAOImpl implements PostDAO {
    private final int RECENT_POSTS_AMOUNT=6;
    private final int AMOUNT_OF_SIMBOLS_IN_CONTENT=200;
    private final int AMOUNT_OF_DATES_IN_ARCHIVE_BLOCK=12;

    private final String SELECT_RECENT="SELECT * FROM post ORDER BY creation_date DESC LIMIT "+RECENT_POSTS_AMOUNT;
    private final String SELECT_MAIN="SELECT * FROM post WHERE ordering>0 ORDER BY ordering";
    private final String SELECT_BY_ID="SELECT * FROM post WHERE id=?";
    private final String SELECT_BY_TAG1="SELECT * FROM post,(SELECT * FROM post_tag WHERE tag_id=";
    private final String SELECT_BY_TAG2=") AS postt WHERE post.id=post_id ORDER BY creation_date DESC LIMIT ";
    private final String SELECT_ALL="SELECT * FROM post  ORDER BY creation_date DESC LIMIT";
    private final String SELECT_COUNT_ALL="SELECT COUNT(*) FROM post";
    private final String SELECT_COUNT_TAG1="SELECT COUNT(*) FROM post,(SELECT * FROM post_tag WHERE tag_id=";
    private final String SELECT_COUNT_TAG2=") AS postt WHERE post.id=post_id";

    private final String SELECT_BY_SEARCH="SELECT * FROM post WHERE";
    private final String SELECT_BY_SEARCH_INTITLE=" title LIKE '%";
    private final String SELECT_BY_SEARCH_INCONTENT=" content LIKE '% ";

    private final String SELECT_DATES="SELECT id,title,creation_date,EXTRACT(YEAR FROM creation_date) AS `year`,EXTRACT(MONTH FROM creation_date) AS `month` FROM post GROUP BY year,month ORDER BY creation_date DESC";
    private final String SELECT_BY_YEAR_AND_MONTH="SELECT * FROM post WHERE EXTRACT(YEAR FROM creation_date)=EXTRACT(YEAR FROM '${date}') AND EXTRACT(MONTH FROM creation_date)=EXTRACT(MONTH FROM '${date}') ORDER BY creation_date DESC";
    private final String SELECT_COUNT_BY_YEAR_AND_MONTH="SELECT COUNT(*) FROM post WHERE EXTRACT(YEAR FROM creation_date)=EXTRACT(YEAR FROM '${date}') AND EXTRACT(MONTH FROM creation_date)=EXTRACT(MONTH FROM '${date}')";

    private Connection con;

    public PostDAOImpl(Connection connection) {
        con=connection;
    }


    public int getAmountOfYearAndMonthPosts(Date date) {
        SimpleDateFormat dateShort=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String dateStr=dateShort.format(date);

        String SQLquery=SELECT_COUNT_BY_YEAR_AND_MONTH;
        SQLquery=SQLquery.replace("${date}",dateStr);

        try {
            Statement stat = con.createStatement();
            ResultSet rs=stat.executeQuery(SQLquery);
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

    public List<Post> getByYearAndMonth(Date date, int start, int limit) {
        List<Post> posts=new ArrayList<Post>();
        try {
            SimpleDateFormat dateShort=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String dateStr=dateShort.format(date);

            String SQLquery=SELECT_BY_YEAR_AND_MONTH;
            SQLquery=SQLquery.replace("${date}",dateStr);

            Statement stat=con.createStatement();
            ResultSet rs=stat.executeQuery(SQLquery + " LIMIT " + (start - 1) + ","+limit);
            while(rs.next())
            {
                Post post=new Post();
                fillValues(post,rs,AMOUNT_OF_SIMBOLS_IN_CONTENT);
                posts.add(post);
            }

            if ((posts!=null)&&(posts.size()>0)) return posts;
            else return null;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Date> getArchiveDates(){
        List<Date> dates=new ArrayList<Date>();
        try {
            Statement stat=con.createStatement();
            ResultSet rs=stat.executeQuery(SELECT_DATES + " LIMIT 0," + AMOUNT_OF_DATES_IN_ARCHIVE_BLOCK);
            while(rs.next())
            {
                Date date=(rs.getTimestamp("creation_date"));
                dates.add(date);
            }
            return dates;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //assistant method for creating WhereClause for Search
    private String createWhereClause(String text, boolean inTitle, boolean inContent)
    {
        //if inTitle==false and inConent=false than search in title
        if(!inTitle&&!inContent) inTitle=true;

        String SQLquery=" WHERE";
        if (inTitle) {
            SQLquery = SQLquery + SELECT_BY_SEARCH_INTITLE + text + "%'";
            if (inContent) {
                SQLquery = SQLquery + " OR "+SELECT_BY_SEARCH_INCONTENT + text + " %'";
            }
        }
        else {
            if (inContent) {
                SQLquery = SQLquery + SELECT_BY_SEARCH_INCONTENT + text + "%'";
            }
        }
        return SQLquery;
    }

    //assistant method for creating SQLquery for Search
    private String createSearchQuery (String text, boolean inTitle, boolean inContent) {
        //if inTitle==false and inConent=false than search in title
        if(!inTitle&&!inContent) inTitle=true;

        String SQLquery=SELECT_BY_SEARCH;
        if (inTitle) {
            SQLquery = SQLquery + SELECT_BY_SEARCH_INTITLE + text + "%'";
            if (inContent) {
                SQLquery = SQLquery + " OR "+SELECT_BY_SEARCH_INCONTENT + text + " %'";
            }
        }
        else {
            if (inContent) {
                SQLquery = SQLquery + SELECT_BY_SEARCH_INCONTENT + text + "%'";
            }
        }
        return SQLquery+" ORDER BY creation_date DESC ";
    }

    public List<Post> getSearched(String text, boolean inTitle, boolean inContent,int start,int limit) {
        String query=createSearchQuery (text,inTitle,inContent);
        List<Post> posts=new ArrayList<Post>();
        try {
            Statement stat=con.createStatement();
            ResultSet rs=stat.executeQuery(query+" LIMIT "+(start-1)+","+limit);
            while(rs.next())
            {
                Post post = new Post();
                fillValues(post,rs,AMOUNT_OF_SIMBOLS_IN_CONTENT);
                posts.add(post);
            }
            if (posts.size()==0) return null;
            else return posts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Post getById(int id) {
        try {
            PreparedStatement pstat = con.prepareStatement(SELECT_BY_ID);
            pstat.setInt(1,id);
            ResultSet rs=pstat.executeQuery();
            Post post = new Post();
            while (rs.next()) {
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setCreationDate(rs.getTimestamp("creation_date"));
                post.setLastUpdateDate(rs.getTimestamp("last_update_date"));
                post.setContent(rs.getString("content"));
                post.setOrdering(rs.getInt("ordering"));
                post.setMark(rs.getBoolean("mark"));
                post.setUserId(rs.getInt("user_id"));
            }
            if (post.getId()==0) return null;
            else return post;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Post getByTitle(String title) {
        return null;
    }

    public List<Post> getRecent() {
        try {
            Statement stat = con.createStatement();
            ResultSet rs=stat.executeQuery(SELECT_RECENT);
            List<Post> posts=new ArrayList<Post>();
            while(rs.next())
            {
                Post post=new Post();
                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));

                posts.add(post);
            }
            return posts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Post> getMain() {
        try {
            Statement stat = con.createStatement();
            ResultSet rs=stat.executeQuery(SELECT_MAIN);
            List<Post> posts=new ArrayList<Post>();
            while(rs.next())
            {
                Post post = new Post();
                fillValues(post, rs, AMOUNT_OF_SIMBOLS_IN_CONTENT);
                posts.add(post);
            }
            return posts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Post> getByTag(int id,int start,int limit) {
        try{
            Statement pstat = con.createStatement();
            ResultSet rs=pstat.executeQuery(SELECT_BY_TAG1 + id+SELECT_BY_TAG2 + (start-1) + "," + limit);
            List<Post> posts = new ArrayList<Post>();
            while(rs.next())
            {
                Post post = new Post();
                fillValues(post,rs,AMOUNT_OF_SIMBOLS_IN_CONTENT);
                posts.add(post);
            }
                if (posts.size()==0) return null;
                else return posts;

        } catch (SQLException e) {
                e.printStackTrace();
        }
        return null;
    }

    public List<Post> getAll(int start,int limit) {
        try {
            Statement pstat = con.createStatement();
            ResultSet rs=pstat.executeQuery(SELECT_ALL + " " + (start-1) + "," + limit);
            List<Post> posts = new ArrayList<Post>();
            while(rs.next())
            {
                Post post = new Post();
                fillValues(post,rs,AMOUNT_OF_SIMBOLS_IN_CONTENT);
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

    public int getAmountOfTagPosts(int id) {
        try {
            Statement stat = con.createStatement();
            ResultSet rs=stat.executeQuery(SELECT_COUNT_TAG1+id+SELECT_COUNT_TAG2);
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

    public int getAmountOfSearchedPosts(String text, boolean inTitle, boolean inContent) {
        String whereClause=createWhereClause(text,inTitle,inContent);
        try {
            Statement stat = con.createStatement();
            ResultSet rs=stat.executeQuery(SELECT_COUNT_ALL+whereClause);
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

    //copy values from ResultSet to object Post
    private void fillValues(Post post,ResultSet rs,int length) throws SQLException{
        post.setId(rs.getInt("id"));
        post.setTitle(rs.getString("title"));
        post.setCreationDate(rs.getTimestamp("creation_date"));
        post.setLastUpdateDate(rs.getTimestamp("last_update_date"));
        Clob content=rs.getClob("content");
        post.setContent(content.getSubString(1,length)+"...");
        post.setOrdering(rs.getInt("ordering"));
        post.setMark(rs.getBoolean("mark"));
        post.setUserId(rs.getInt("user_id"));
    }



}


