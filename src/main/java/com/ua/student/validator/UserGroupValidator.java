package com.ua.student.validator;

import com.ua.student.dao.UserGroupsDAO;
import com.ua.student.model.UserGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserGroupValidator implements Validator {

    @Autowired
    UserGroupsDAO userGroupsDAO;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserGroups.class;
    }

    @Override
    public void validate(Object target, Errors errors) {

        UserGroups userGroups = (UserGroups) target;

        if (userGroupsDAO.checkGroupAvailability(userGroups.getNumberOfGroup()) == 0) {
            errors.rejectValue("numberOfGroup", "numberOfGroup.incorrect", "Группа с таким номером не найдена!");
        }

        if (userGroupsDAO.checkExistenceOfRequest(userGroups.getNumberOfGroup(), userGroups.getId()) != 0) {
            errors.rejectValue("numberOfGroup", "numberOfGroup.incorrect", "Заявка на вступление в эту группу уже создана!");
        }

        if (userGroupsDAO.checkExistenceUserInUserGroup(userGroups.getNumberOfGroup(), userGroups.getId()) != 0) {
            errors.rejectValue("numberOfGroup", "numberOfGroup.incorrect", "Вы уже есть в этой группе!");
        }
    }
}