package com.example.managerstaff.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Comment {
    @SerializedName("id_comment")
    @Expose
    private int idComment;
    @SerializedName("time_cmt")
    @Expose
    private String time_cmt;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("post")
    @Expose
    private int idPost;
    @SerializedName("user")
    @Expose
    private int idUser;

    public Comment() {
    }

    public Comment(int idComment, String time_cmt, String content, int idPost, int idUser) {
        this.idComment = idComment;
        this.time_cmt = time_cmt;
        this.content = content;
        this.idPost = idPost;
        this.idUser = idUser;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public String getTime_cmt() {
        return time_cmt;
    }

    public void setTime_cmt(String time_cmt) {
        this.time_cmt = time_cmt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
