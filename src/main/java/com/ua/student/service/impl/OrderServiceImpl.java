package com.ua.student.service.impl;

import com.ua.student.dao.OrderDAO;
import com.ua.student.dao.UserDAO;
import com.ua.student.model.*;
import com.ua.student.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private UserDAO userDAO;

    public OrderServiceImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public OrderItems findItemById(User user, int id, List<OrderItems> orderItems) {
        for (OrderItems item : orderItems) {
            if (item.getDish().getId() == id && user.equals(item.getUser())) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void addDish(OrderItems currentItem, List<OrderItems> orderItems) {

        OrderItems item = findItemById(currentItem.getUser(), currentItem.getDish().getId(), orderItems);

        if (item == null) {
            item = new OrderItems();
            item.setQuantity(0);
            item.setDish(currentItem.getDish());
            item.setPrice(currentItem.getDish().getPrice());
            item.setUser(currentItem.getUser());
            orderItems.add(item);
        }

        int newQuantity = item.getQuantity() + currentItem.getQuantity();

        if (newQuantity <= 0) {
            orderItems.remove(item);
        } else {
            item.setQuantity(newQuantity);
            item.setSum(currentItem.getPrice() * newQuantity);
        }
    }

    @Override
    public void updateDishInOrder(User user, int id, int quantity, List<OrderItems> orderItems) {

        OrderItems item = findItemById(user, id, orderItems);

        if (item != null) {
            if (quantity <= 0) {
                orderItems.remove(item);
            } else {
                item.setQuantity(quantity);
                item.setSum(item.getDish().getPrice() * quantity);
            }
        }
    }

    @Override
    public void removeDish(User user, Dish dish, List<OrderItems> orderItems) {
        OrderItems item = findItemById(user, dish.getId(), orderItems);
        if (item != null) {
            orderItems.remove(item);
        }
    }

    @Override
    public double getSumTotal(List<OrderItems> orderItems) {
        double total = 0;
        for (OrderItems item : orderItems) {
            total += item.getSum();
        }
        return total;
    }

    @Override
    public void updateQuantity(User user, Order order, List<OrderItems> orderItems) {
        if (order != null) {
            List<OrderItems> items = order.getOrderItems();
            for (OrderItems item : items) {
                if (item.getUser().getId() == user.getId()) {
                    updateDishInOrder(user, item.getDish().getId(), item.getQuantity(), orderItems);
                }
            }
        }
    }

    @Override
    public Order getOrderInSession(HttpServletRequest request) {

        Order order = (Order) request.getSession().getAttribute("myOrder");

        if (order == null) {
            order = new Order();
            request.getSession().setAttribute("myOrder", order);
        }

        return order;
    }

    @Override
    public void setOrderInSession(HttpServletRequest request, Order order) {
        if (order != null) {
            request.getSession().setAttribute("myOrder", order);
        }
    }

    @Override
    public void removeOrderInSession(HttpServletRequest request) {
        request.getSession().removeAttribute("myOrder");
    }

    @Override
    public String saveOrder(Order orderForm, HttpServletRequest request, Authentication authentication, Model model, OrderStatuses orderStatuses) {

        Order order = getOrderInSession(request);
        updateQuantity(userDAO.getUserByUsername(authentication.getName()), orderForm, order.getOrderItems());
        order.setSum(getSumTotal(order.getOrderItems()));

        model.addAttribute("sumTotal", order.getSum());

        if (order.getNumberOfOrder() != 0) {
            orderDAO.update(order, orderStatuses);
        } else {
            orderDAO.insert(order, 0, orderStatuses);
        }

        removeOrderInSession(request);

        model.addAttribute("title", "Заказ сохранен");
        model.addAttribute("message", "Спасибо за заказ. Номер вашего заказа: "+order.getNumberOfOrder());
        return "message";
    }
}
