package com.example.piso.noteapp.Models;

/**
 * Created by Piso on 12/11/2016.
 */
public class Comment {

    private String photo   ;
    private String name    ;
    private String time    ;
    private String content ;

    public void setName(String name) {
        this.name = name;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getName() {
        return name;
    }
    public String getTime() {
        return time;
    }
    public String getContent() {
        return content;
    }
    public String getPhoto() {
        return photo;
    }
}
