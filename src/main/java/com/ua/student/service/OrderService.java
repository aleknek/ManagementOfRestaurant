package com.ua.student.service;

import com.ua.student.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {

    OrderItems findItemById(User user, int id, List<OrderItems>  orderItems);

    void addDish(OrderItems item, List<OrderItems>  orderItems);

    void updateDishInOrder(User user, int id, int quantity, List<OrderItems> orderItems);

    void removeDish(User user, Dish dish, List<OrderItems>  orderItems);

    double getSumTotal(List<OrderItems>  orderItems);

    void updateQuantity(User user, Order order, List<OrderItems>  orderItems);

    Order getOrderInSession(HttpServletRequest request);

    void removeOrderInSession(HttpServletRequest request);

    void setOrderInSession(HttpServletRequest request, Order order);

    String saveOrder(Order orderForm, HttpServletRequest request, Authentication authentication, Model model, OrderStatuses orderStatuses);
}
