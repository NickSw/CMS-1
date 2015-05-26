package ua.demo.dao;

import ua.demo.entity.Post;

import java.util.Date;
import java.util.List;

public interface PostDAO {
    public Post getById(int id);
    public Post getByTitle(String title);
    public List<Post> getRecent();
    public List<Post> getMain();
    public List<Post> getByTag(int id,int start,int limit);
    public List<Post> getAll(int start,int limit);
    public int getAmountOfAllPosts();
    public int getAmountOfTagPosts(int id);
    public int getAmountOfSearchedPosts(String text,boolean inTitle,boolean inContent);
    public List<Post> getSearched(String text,boolean inTitle,boolean inContent,int start,int limit);
    public List<Post> getByYearAndMonth(Date date,int start,int limit);
    public List<Date> getArchiveDates();
    public int getAmountOfYearAndMonthPosts(Date date);


}
