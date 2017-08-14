package com.ua.student.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderGroup extends Order implements Serializable{

    private UserGroups userGroup;

    public OrderGroup() {
    }

    public OrderGroup(UserGroups userGroup) {
        this.userGroup = userGroup;
    }

    public List<User> getUserList() {
        List<User> newUserList = new ArrayList<>();
        List<OrderItems> orderItems = getOrderItems();
        for (OrderItems item : orderItems) {
            User user = item.getUser();
            if (!newUserList.contains(user)) {
                newUserList.add(user);
            }
        }
        return newUserList;
    }

    public void setUserGroup(UserGroups userGroup) {
        this.userGroup = userGroup;
    }

    public UserGroups getUserGroup() {
        return userGroup;
    }

    public double getSum(User user){
        double total = 0.0;
        List<OrderItems> orderItems = getOrderItems();
        for (OrderItems item : orderItems) {
            if (user.equals(item.getUser())) {
                total += item.getSum();
            }
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderGroup)) return false;
        if (!super.equals(o)) return false;

        OrderGroup that = (OrderGroup) o;

        return getUserGroup() != null ? getUserGroup().equals(that.getUserGroup()) : that.getUserGroup() == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getUserGroup() != null ? getUserGroup().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderGroup{" +
                "userGroup=" + userGroup +
                '}';
    }
}
