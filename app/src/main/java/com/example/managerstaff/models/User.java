package com.example.managerstaff.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class User {
    @SerializedName("id_user")
    @Expose
    private int idUser;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("wage")
    @Expose
    private double wage;
    @SerializedName("comments")
    @Expose
    private ArrayList<Comment> listComments;
    @SerializedName("posts")
    @Expose
    private ArrayList<Post> listPosts;
    @SerializedName("time_ins")
    @Expose
    private ArrayList<TimeIn> listTimeIns;
    @SerializedName("time_outs")
    @Expose
    private ArrayList<TimeOut> listTimeOuts;
    @SerializedName("account")
    @Expose
    private Account account;
    @SerializedName("part")
    @Expose
    private Part part;
    @SerializedName("position")
    @Expose
    private Position position;

    public User() {
    }

    public User(int idUser, String avatar, String fullName, String birthday, String gender, String address, String email, String phone, double wage, ArrayList<Comment> listComments, ArrayList<Post> listPosts, ArrayList<TimeIn> listTimeIns, ArrayList<TimeOut> listTimeOuts, Account account, Part part, Position position) {
        this.idUser = idUser;
        this.avatar = avatar;
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.wage = wage;
        this.listComments = listComments;
        this.listPosts = listPosts;
        this.listTimeIns = listTimeIns;
        this.listTimeOuts = listTimeOuts;
        this.account = account;
        this.part = part;
        this.position = position;
    }

    public int getId() {
        return idUser;
    }

    public void setId(int idUser) {
        this.idUser = idUser;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public ArrayList<Comment> getListComments() {
        return listComments;
    }

    public void setListComments(ArrayList<Comment> listComments) {
        this.listComments = listComments;
    }

    public ArrayList<Post> getListPosts() {
        return listPosts;
    }

    public void setListPosts(ArrayList<Post> listPosts) {
        this.listPosts = listPosts;
    }

    public ArrayList<TimeIn> getListTimeIns() {
        return listTimeIns;
    }

    public void setListTimeIns(ArrayList<TimeIn> listTimeIns) {
        this.listTimeIns = listTimeIns;
    }

    public ArrayList<TimeOut> getListTimeOuts() {
        return listTimeOuts;
    }

    public void setListTimeOuts(ArrayList<TimeOut> listTimeOuts) {
        this.listTimeOuts = listTimeOuts;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
