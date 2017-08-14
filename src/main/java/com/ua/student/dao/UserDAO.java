package com.ua.student.dao;

import com.ua.student.model.User;

public interface UserDAO {

    void insert(User user);

    void update(User user);

    User getUserByUsername(String username);

    User getAdminById(int id);

    User getUserById(int id);

}
