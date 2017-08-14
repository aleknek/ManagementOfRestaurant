package com.ua.student.dao.impl;

import com.ua.student.dao.rowMapper.AdminRowMapper;
import com.ua.student.dao.UserDAO;
import com.ua.student.dao.rowMapper.UserRowMapper;
import com.ua.student.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class UserDAOImpl implements UserDAO {

    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();
    private static final AdminRowMapper ADMIN_ROW_MAPPER = new AdminRowMapper();

    private JdbcTemplate jdbcTemplate;

    public UserDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(User user) {

        String sql_user = "INSERT INTO users (username, password, email, phone, firstname, lastname)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql_user, user.getUsername(), user.getPassword(), user.getEmail(), user.getPhone(),
                user.getFirstName(), user.getLastName());

        String sql_role = "INSERT INTO user_roles (id_role, id_user)"
                + " VALUES (?, ?)";
        jdbcTemplate.update(sql_role, 2, getUserByUsername(user.getUsername()).getId());
    }

    @Override
    public void update(User user) {

        String sql = "UPDATE users SET username=?, password=?, email=?, phone=?, firstname=?, lastname=?  WHERE id=?";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getEmail(), user.getPhone(),
                user.getFirstName(), user.getLastName(), user.getId());
    }

    @Override
    public User getUserByUsername(String name) {
        String sql = "SELECT users.id, users.username, users.email, users.phone, users.firstname, users.lastname " +
                "FROM db_restaurant.users AS users WHERE users.username = '" + name + "'";
        try {
            return jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User getAdminById(int id) {

        String sql = "SELECT users.id, users.username, users.email, users.phone, users.firstname, users.lastname " +
                "FROM db_restaurant.users AS users WHERE users.id = '" + id + "'";
        return jdbcTemplate.queryForObject(sql, ADMIN_ROW_MAPPER);
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT users.id, users.username, users.email, users.phone, users.firstname, users.lastname " +
                "FROM db_restaurant.users AS users WHERE users.id = '" + id + "'";
        return jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER);
    }
}
