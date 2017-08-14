package com.ua.student.validator;

import com.ua.student.dao.UserDAO;
import com.ua.student.model.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    UserDAO userDAO;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == User.class;
    }

    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;

        if (user.getUsername().isEmpty()) {
            errors.rejectValue("username", "username.empty", "Login должен быть заполнен!");
        }

        if (userDAO.getUserByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.username", "Такой login уже занят!");
        }

        if (user.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.empty", "Пароль должен быть заполнен!");
        }

        if (user.getFirstName().isEmpty()) {
            errors.rejectValue("firstName", "firstName.empty", "Имя должно быть заполнено!");
        }

        if (user.getLastName().isEmpty()) {
            errors.rejectValue("lastName", "lastName.empty", "Фамилия должна быть заполнена!");
        }

        if (user.getPhone().isEmpty()) {
            errors.rejectValue("phone", "phone.empty", "Телефон должен быть заполнен!");
        }

        if( !EmailValidator.getInstance().isValid(user.getEmail() ) ){
			errors.rejectValue("email", "email.notValid", "Некорректный email адрес!");
		}
    }
}