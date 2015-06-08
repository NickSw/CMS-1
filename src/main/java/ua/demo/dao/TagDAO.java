package ua.demo.dao;

import ua.demo.entity.Tag;
import java.util.List;

public interface TagDAO {
    public Tag getById(int id);
    public List<Tag> getAll();
    public List<Tag> getByPost(int id);
    public boolean addTag(Tag tag);
    public boolean deleteById(int id);
}
