package com.ua.student.dao;

import com.ua.student.model.*;

import java.util.List;

public interface OrderDAO {

    void insert(Order order, int numberOfGroup, OrderStatuses orderStatuses);

    void update(Order order, OrderStatuses orderStatuses);

    Order getOrderByNumberOfOrder(int numberOfOrder);

    List<Order> getListOrders();

    List<Order> getListMyOrders(User user);

    List<OrderItems> getOrderItemsByNumberOfOrder(int numberOfOrder);

    int getNewNumberOfOrder();

    int isOrderSent(int numberOfOrder);

    int checkAvailabilityGroupOrder(int numberOfOrder, int idUser);

}
