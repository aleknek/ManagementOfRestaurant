package com.ua.student.dao.impl;

import com.ua.student.dao.*;
import com.ua.student.dao.rowMapper.UserRowMapper;
import com.ua.student.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserGroupsDAOImpl implements UserGroupsDAO {

    @Autowired
    private UserDAO userDAO;

    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();

    private JdbcTemplate jdbcTemplate;

    public UserGroupsDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(int idUser) {

        int numberOfGroup = getNewNumberOfUserGroup() + 1;

        String sqlNewGroup = "INSERT INTO db_restaurant.groups (id_user_admin, numberOfGroup, active)"
                + " VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlNewGroup, idUser, numberOfGroup, 1);

        insertNewUserToGroupMembers(numberOfGroup, idUser);
    }

    @Override
    public void update(UserGroups userGroup) {
    }

    public void insertNewUserToGroupMembers(int numberOfGroup, int idUser) {
        String sqlNewGroupMembers = "INSERT INTO db_restaurant.group_members (numberOfGroup, id_user, active)"
                + " VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlNewGroupMembers, numberOfGroup, idUser, 1);
    }

    @Override
    public void delete(int numberOfGroup) {
        jdbcTemplate.update("UPDATE group_members SET active = 0 WHERE numberOfGroup = " + numberOfGroup);
        jdbcTemplate.update("UPDATE groups SET active = 0 WHERE numberOfGroup = " + numberOfGroup);
    }

    @Override
    public List<UserGroups> getUserGroupsByIdUser(int idUser) {

        String sql = "SELECT groups.id AS id, groups.numberOfGroup AS numberOfGroup, groups.id_user_admin AS id_user_admin " +
                "FROM db_restaurant.groups AS groups LEFT JOIN db_restaurant.group_members AS group_members " +
                "ON groups.numberOfGroup = group_members.numberOfGroup " +
                "WHERE group_members.id_user = " + idUser + " AND groups.active = 1 " +
                "ORDER BY numberOfGroup DESC";

        return rowMapperQuery(sql);

    }

    @Override
    public List<UserGroups> getAllUserGroups() {

        String sql = "SELECT groups.id AS id, groups.numberOfGroup AS numberOfGroup, groups.id_user_admin AS id_user_admin " +
                "FROM db_restaurant.groups AS groups LEFT JOIN db_restaurant.group_members AS group_members " +
                "ON groups.numberOfGroup = group_members.numberOfGroup " +
                "WHERE groups.active = " + 1 + " " +
                "GROUP BY numberOfGroup " +
                "ORDER BY numberOfGroup DESC";

        return rowMapperQuery(sql);

    }

    @Override
    public UserGroups getUserGroupByNumberOfGroup(int numberOfGroup) {
        String sql = "SELECT * FROM db_restaurant.groups WHERE groups.numberOfGroup = " + numberOfGroup;
        try {
            return rowMapperQueryForObject(sql);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<UserGroups> getNewUsersOfMyGroups(int idUser) {

        String sql = "SELECT group_members_new.numberOfGroup, groups.id FROM group_members_new " +
                "LEFT JOIN groups " +
                "ON group_members_new.numberOfGroup = groups.numberOfGroup " +
                "WHERE groups.id_user_admin  = " + idUser + " AND group_members_new.rejected = " + 0 + " " +
                "GROUP BY numberOfGroup, id";

        return jdbcTemplate.query(sql, new RowMapper<UserGroups>() {
            @Override
            public UserGroups mapRow(ResultSet resultSet, int i) throws SQLException {
                UserGroups userGroups = new UserGroups();
                userGroups.setId(resultSet.getInt("id"));
                userGroups.setNumberOfGroup(resultSet.getInt("numberOfGroup"));
                userGroups.setUser(userDAO.getAdminById(idUser));
                userGroups.setUsers(getNewUsersOfMyGroupsDetail(resultSet.getInt("numberOfGroup")));
                return userGroups;
            }
        });
    }

    public List<User> getNewUsersOfMyGroupsDetail(int numberOfGroup) {

        String sql = "SELECT users.id, users.username, users.email, users.phone, users.lastname," +
                "users.firstname FROM db_restaurant.group_members_new AS group_members_new " +
                "LEFT JOIN db_restaurant.users AS users " +
                "ON group_members_new.id_user = users.id " +
                "WHERE group_members_new.numberOfGroup = " + numberOfGroup + " AND group_members_new.rejected = " + 0;
        try {
            return jdbcTemplate.query(sql, USER_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }


    @Override
    public List<User> getCompositionUserGroup(int numberOfGroup) {

        String sql = "SELECT users.id, users.firstname, users.lastname, users.username, users.phone, users.email " +
                "FROM db_restaurant.group_members AS group_members " +
                "LEFT JOIN users AS users " +
                "ON group_members.id_user = users.id " +
                "WHERE group_members.numberOfGroup = " + numberOfGroup;
        try {
            return jdbcTemplate.query(sql, USER_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void quitFromGroup(int numberOfGroup, int idUser) {
        jdbcTemplate.update("DELETE FROM group_members WHERE numberOfGroup = " + numberOfGroup + " AND id_user = " + idUser);
    }

    @Override
    public void acceptNewUserOfGroup(int numberOfGroup, int idUser) {

        insertNewUserToGroupMembers(numberOfGroup, idUser);

        String sql = "DELETE FROM db_restaurant.group_members_new WHERE numberOfGroup = " + numberOfGroup + " " +
                "AND id_user = " + idUser;
        jdbcTemplate.update(sql);
    }

    @Override
    public void rejectNewUserOfGroup(int numberOfGroup, int idUser) {
        String sql = "UPDATE db_restaurant.group_members_new SET rejected = ? WHERE numberOfGroup= ? AND id_user = ?";
        jdbcTemplate.update(sql, 1, numberOfGroup, idUser);
    }

    @Override
    public int getNewNumberOfUserGroup() {
        String sql = "SELECT IFNULL(max(numberOfGroup),0) as numberOfGroup FROM db_restaurant.groups ";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public void addUnconfirmedUser(int numberOfGroup, int idUser) {
        String sqlNewGroup = "INSERT INTO db_restaurant.group_members_new (id_user, numberOfGroup, rejected)"
                + " VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlNewGroup, idUser, numberOfGroup, 0);
    }

    @Override
    public int checkExistenceOfRequest(int numberOfGroup, int idUser) {
        String sql = "SELECT IFNULL(max(groupMembersNew.numberOfGroup), 0) AS numberOfGroup FROM " +
                "db_restaurant.group_members_new AS groupMembersNew WHERE numberOfGroup = " + numberOfGroup + " AND id_user = "
                + idUser + " AND rejected = " + 0;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public int checkGroupAvailability(int numberOfGroup) {
        return jdbcTemplate.queryForObject("SELECT IFNULL(max(userGroup.numberOfGroup), 0) AS numberOfGroup FROM db_restaurant.groups AS userGroup WHERE userGroup.numberOfGroup = " + numberOfGroup, Integer.class);
    }

    @Override
    public int checkExistenceUserInUserGroup(int numberOfGroup, int idUser) {
        String sql = "SELECT IFNULL(max(group_members.numberOfGroup), 0) AS numberOfGroup FROM db_restaurant.group_members" +
                " AS group_members WHERE group_members.numberOfGroup = " + numberOfGroup + " AND id_user = " + idUser;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public UserGroups rowMapperQueryForObject(String sql) {
        return jdbcTemplate.queryForObject(sql, new RowMapper<UserGroups>() {
            @Override
            public UserGroups mapRow(ResultSet resultSet, int i) throws SQLException {
                return rowMapper(resultSet);
            }
        });
    }

    public List<UserGroups> rowMapperQuery(String sql) {
        return jdbcTemplate.query(sql, new RowMapper<UserGroups>() {
            @Override
            public UserGroups mapRow(ResultSet resultSet, int i) throws SQLException {
                return rowMapper(resultSet);
            }
        });
    }

    public UserGroups rowMapper(ResultSet resultSet) throws SQLException {
        UserGroups userGroups = new UserGroups();
        userGroups.setId(resultSet.getInt("id"));
        userGroups.setNumberOfGroup(resultSet.getInt("numberOfGroup"));
        userGroups.setUser(userDAO.getAdminById(resultSet.getInt("id_user_admin")));
        userGroups.setUsers(getCompositionUserGroup(userGroups.getNumberOfGroup()));
        return userGroups;
    }
}
