package com.ua.student.web;

import com.ua.student.dao.UserDAO;
import com.ua.student.dao.UserGroupsDAO;
import com.ua.student.model.*;
import com.ua.student.validator.UserGroupValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserGroupsController {

    @Autowired
    private UserGroupsDAO userGroupsDAO;

    @Autowired
    private UserGroupValidator userGroupValidator;

    @Autowired
    UserDAO userDAO;

    @InitBinder("userGroupForm")
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        if (target.getClass() == UserGroups.class) {
            dataBinder.setValidator(userGroupValidator);
        }
    }

    @RequestMapping(value = {"/userGroups"}, method = RequestMethod.GET)
    public String userGroups(Model model, Authentication authentication) {
        User currentUser = userDAO.getUserByUsername(authentication.getName());
        List<UserGroups> userGroups = userGroupsDAO.getUserGroupsByIdUser(currentUser.getId());
        if (userGroups.isEmpty()) {
            model.addAttribute("message", "Вы не состоите ни в одной группе.");
            model.addAttribute("title", "Группы пользователей");
            return "message";
        }
        model.addAttribute("userGroups", userGroups);
        return "userGroup";
    }

    @RequestMapping(value = {"/userGroupsAll"}, method = RequestMethod.GET)
    public String userGroupsAll(Model model) {
        List<UserGroups> userGroups = userGroupsDAO.getAllUserGroups();
        model.addAttribute("userGroups", userGroups);
        return "userGroup";
    }

    @RequestMapping(value = {"/quitGroup"}, method = RequestMethod.GET)
    public String quitFromGroup(@RequestParam(value = "numberOfGroup") String numberOfGroup,
                                Authentication authentication) {
        userGroupsDAO.quitFromGroup(Integer.valueOf(numberOfGroup), userDAO.getUserByUsername(authentication.getName()).getId());
        return "redirect:/userGroups";
    }

    @RequestMapping(value = {"/deleteGroup"}, method = RequestMethod.GET)
    public String deleteGroup(@RequestParam(value = "numberOfGroup", defaultValue = "") String numberOfGroup) {
        userGroupsDAO.delete(Integer.valueOf(numberOfGroup));
        return "redirect:/userGroups";
    }

    @RequestMapping(value = {"/getNewUsersOfMyGroup"}, method = RequestMethod.GET)
    public String getNewUsersOfMyGroup(Model model, Authentication authentication) {

        List<UserGroups> newUsersForMyGroups = userGroupsDAO.getNewUsersOfMyGroups(userDAO.getUserByUsername(authentication.getName()).getId());
        if (newUsersForMyGroups.isEmpty()) {
            model.addAttribute("title", "Новые пользователи");
            model.addAttribute("message", "Нет новых заявок на вступление в ваши группы");
            return "message";
        }

        model.addAttribute("newUsersForMyGroups", newUsersForMyGroups);
        return "newUsersForMyGroups";
    }

    @RequestMapping(value = {"/acceptNewUserOfGroup"}, method = RequestMethod.GET)
    public String acceptNewUserOfGroup(@RequestParam(value = "idUser", defaultValue = "") String idUser,
                                       @RequestParam(value = "numberOfGroup", defaultValue = "") String numberOfGroup) {
        userGroupsDAO.acceptNewUserOfGroup(Integer.valueOf(numberOfGroup), Integer.valueOf(idUser));
        return "redirect:/getNewUsersOfMyGroup";
    }

    @RequestMapping(value = {"/rejectNewUserOfGroup"}, method = RequestMethod.GET)
    public String rejectNewUserOfGroup(@RequestParam(value = "idUser", defaultValue = "") String idUser,
                                       @RequestParam(value = "numberOfGroup", defaultValue = "") String numberOfGroup) {
        userGroupsDAO.rejectNewUserOfGroup(Integer.valueOf(numberOfGroup), Integer.valueOf(idUser));
        return "redirect:/getNewUsersOfMyGroup";
    }

    @RequestMapping(value = {"/createNewGroup"}, method = RequestMethod.GET)
    public String createNewGroup(Model model, Authentication authentication) {
        User currentUser = userDAO.getUserByUsername(authentication.getName());
        userGroupsDAO.insert(currentUser.getId());
        List<UserGroups> userGroups = userGroupsDAO.getUserGroupsByIdUser(currentUser.getId());
        model.addAttribute("userGroups", userGroups);
        return "userGroup";
    }

    @RequestMapping(value = {"/joinToGroupUsers"}, method = RequestMethod.GET)
    public String joinToGroupUsers(ModelMap model, Authentication authentication) {
        UserGroups userGroup = new UserGroups();
        userGroup.setId(userDAO.getUserByUsername(authentication.getName()).getId());
        model.put("userGroupForm", userGroup);
        return "joinToGroupUsers";
    }

    @RequestMapping(value = {"/joinToGroupUsers"}, method = RequestMethod.POST)
    public String joinToGroupUsers(Model model,
                                   @ModelAttribute("userGroupForm") @Validated UserGroups userGroup,
                                   BindingResult result,
                                   Authentication authentication) {

        if (result.hasErrors()) {
            return "joinToGroupUsers";
        }

        userGroupsDAO.addUnconfirmedUser(userGroup.getNumberOfGroup(), userDAO.getUserByUsername(authentication.getName()).getId());
        model.addAttribute("title", "Присоединиться к группе пользователей");
        model.addAttribute("message", "Заявка на вступление в группу отправлена.");

        return "message";
    }
}
