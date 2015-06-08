package ua.demo.dao;

import ua.demo.entity.Post;

import java.util.List;

/**
 * Created by Sergey on 04.06.2015.
 */
public interface PostAdminDAO {
    public List<Post> getAll(int start,int limit,int order);
    public int getAmountOfAllPosts();
    public int updatePost(Post post);
    public int addPost(Post post);
    public boolean deleteById(int id);
    public Post getById(int id);
}
