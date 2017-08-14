package com.ua.student.web;

import com.ua.student.dao.UserDAO;
import com.ua.student.model.User;
import com.ua.student.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/signup")
public class UserController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    UserDAO userDAO;

    @InitBinder("userForm")
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        if (target.getClass() == User.class) {
            dataBinder.setValidator(userValidator);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String signup(ModelMap model, @RequestParam(value = "username", defaultValue = "") String username) {

        User user = null;

        if (username != null && username.length() > 0) {
            user = userDAO.getUserByUsername(username);
        }
        if (user == null) {
            user = new User();
        }

        model.put("userForm", user);
        return "signup";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSignup(ModelMap model, @ModelAttribute("userForm") @Validated User user, BindingResult result) {

        if (result.hasErrors()) {
            return "signup";
        }

        if (user.getId() == 0) {
            userDAO.insert(user);
            return "login";
        } else {
            userDAO.update(user);
            return "account";
        }
    }
}
