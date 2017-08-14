package com.ua.student.validator;

import com.ua.student.dao.OrderDAO;
import com.ua.student.dao.UserDAO;
import com.ua.student.model.Order;
import com.ua.student.model.OrderGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OrderGroupValidator implements Validator {

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    UserDAO userDAO;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == OrderGroup.class;
    }

    @Override
    public void validate(Object target, Errors errors) {

        Order order = (OrderGroup) target;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (orderDAO.getOrderByNumberOfOrder(order.getNumberOfOrder()) == null) {
            errors.rejectValue("numberOfOrder", "numberOfOrder.incorrect", "Заказ с таким номером не найден!");
        }

        if (orderDAO.isOrderSent(order.getNumberOfOrder()) == 2) {
            errors.rejectValue("numberOfOrder", "numberOfOrder.incorrect", "Заказ с таким номером уже отправлен!");
        }

        if (!(orderDAO.getOrderByNumberOfOrder(order.getNumberOfOrder()) instanceof OrderGroup)) {
            errors.rejectValue("numberOfOrder", "numberOfOrder.incorrect", "Указанный номер группового заказа не является групповым заказом. Уточните номер заказа и повторите еще раз!");
        }

        if (orderDAO.checkAvailabilityGroupOrder(order.getNumberOfOrder(),
                userDAO.getUserByUsername(userDetails.getUsername()).getId()) == 0) {
            errors.rejectValue("numberOfOrder", "numberOfOrder.incorrect", "Вы не можете присоединиться к этому заказу, т.к. вы не состоите в пользовательской группе данного заказа!");
        }
    }
}