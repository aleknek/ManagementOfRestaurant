package com.ua.student.model;

import java.io.Serializable;
import java.util.List;

public class UserGroups implements Serializable{

    private int id;
    private int numberOfGroup;
    private User user;

    private List<User> users;

    public UserGroups() {
    }

    public UserGroups(int id, int numberOfGroup, User user, List<User> users) {
        this.id = id;
        this.numberOfGroup = numberOfGroup;
        this.user = user;
        this.users = users;
    }

    public int getNumberOfGroup() {
        return numberOfGroup;
    }

    public void setNumberOfGroup(int numberOfGroup) {
        this.numberOfGroup = numberOfGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
