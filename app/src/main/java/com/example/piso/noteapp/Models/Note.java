package com.example.piso.noteapp.Models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Piso on 12/11/2016.
 */
public class Note  implements Serializable {
    private String photo ;
    private String Topic ;
    private String Time  ;
    private String title ;
    private boolean faculty ;
    private int Likes ;
    private  String content  ;
    private String Id ;
    private String PublisherId ;
    private String Publishername ;

    public String getPublishername() {
        return Publishername;
    }

    public void setPublishername(String publishername) {
        Publishername = publishername;
    }

    public String getId() {
        return Id;
    }


    public String getPublisherId() {
        return PublisherId;
    }

    public void setPublisherId(String publisherId) {
        PublisherId = publisherId;
    }

    public void setId(String id) {
        Id = id;
    }

    List<Comment> Comments ;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFaculty() {
        return faculty;
    }

    public void setFaculty(boolean faculty) {
        this.faculty = faculty;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
