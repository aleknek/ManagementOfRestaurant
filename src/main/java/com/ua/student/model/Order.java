package com.ua.student.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

    private int id;
    private OrderStatuses orderStatus;
    private Date dateOfOrder;
    private int numberOfOrder;
    private double sum;

    private final List<OrderItems> orderItems = new ArrayList<>();

    public Order() {
    }

    public Order(int id, OrderStatuses orderStatus, Date dateOfOrder, int numberOfOrder, double sum) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.dateOfOrder = dateOfOrder;
        this.numberOfOrder = numberOfOrder;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderStatuses getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatuses orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public double getSum() {
        return sum;
    }

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public int getNumberOfOrder() {
        return numberOfOrder;
    }

    public void setNumberOfOrder(int numberOfOrder) {
        this.numberOfOrder = numberOfOrder;
    }

    public double getSum(User user) {
        return this.getSum();
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        if (!orderItems.isEmpty()) {
            userList.add(orderItems.get(0).getUser());
        }
        return userList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        if (getId() != order.getId()) return false;
        if (getNumberOfOrder() != order.getNumberOfOrder()) return false;
        if (Double.compare(order.getSum(), getSum()) != 0) return false;
        if (getOrderStatus() != order.getOrderStatus()) return false;
        if (getDateOfOrder() != null ? !getDateOfOrder().equals(order.getDateOfOrder()) : order.getDateOfOrder() != null)
            return false;
        return getOrderItems() != null ? getOrderItems().equals(order.getOrderItems()) : order.getOrderItems() == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId();
        result = 31 * result + (getOrderStatus() != null ? getOrderStatus().hashCode() : 0);
        result = 31 * result + (getDateOfOrder() != null ? getDateOfOrder().hashCode() : 0);
        result = 31 * result + getNumberOfOrder();
        temp = Double.doubleToLongBits(getSum());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getOrderItems() != null ? getOrderItems().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", dateOfOrder=" + dateOfOrder +
                ", numberOfOrder=" + numberOfOrder +
                ", sum=" + sum +
                ", orderItems=" + orderItems +
                '}';
    }
}
