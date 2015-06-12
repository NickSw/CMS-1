package ua.demo.entity;

import java.util.Date;

/*
*
* Created by Sergey on 14.05.2015.
*/

public class Post extends Entity{
    private String title;
    private Date creationDate;
    private Date lastUpdateDate;
    private String content;
    private int ordering;
    private boolean mark;
    private int userId;
    private String userLogin;
    private int tagId1;
    private int tagId2;
    private int tagId3;


    //getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public boolean getMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public int getTagId1() {
        return tagId1;
    }

    public void setTagId1(int tagId1) {
        this.tagId1 = tagId1;
    }

    public int getTagId2() {
        return tagId2;
    }

    public void setTagId2(int tagId2) {
        this.tagId2 = tagId2;
    }

    public int getTagId3() {
        return tagId3;
    }

    public void setTagId3(int tagId3) {
        this.tagId3 = tagId3;
    }
}
