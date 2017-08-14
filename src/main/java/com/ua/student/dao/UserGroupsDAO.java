package com.ua.student.dao;

import com.ua.student.model.User;
import com.ua.student.model.UserGroups;

import java.util.List;

public interface UserGroupsDAO {

    void insert(int idUser);

    void update(UserGroups userGroup);

    void delete(int id);

    List<UserGroups> getUserGroupsByIdUser(int idUser);

    List<UserGroups> getAllUserGroups();

    UserGroups getUserGroupByNumberOfGroup(int numberOfGroup);

    List<UserGroups> getNewUsersOfMyGroups(int idUser);

    void insertNewUserToGroupMembers(int numberOfGroup, int idUser);

    int getNewNumberOfUserGroup();

    List<User> getCompositionUserGroup(int idGroup);

    void quitFromGroup(int numberOfGroup, int idUser);

    void acceptNewUserOfGroup(int idUserGroup, int idUser);

    void rejectNewUserOfGroup(int idUserGroup, int idUser);

    void addUnconfirmedUser(int numberOfGroup, int idUser);

    int checkGroupAvailability(int numberOfGroup);

    int checkExistenceOfRequest(int numberOfGroup, int idUser);

    int checkExistenceUserInUserGroup(int numberOfGroup, int idUser);
}
