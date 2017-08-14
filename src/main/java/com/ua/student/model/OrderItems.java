package com.ua.student.model;

import java.io.Serializable;

public class OrderItems implements Serializable {

    private int id;
    private Dish dish;
    private User user;
    private int quantity;
    private double price;
    private double sum;

    public OrderItems() {
    }

    public OrderItems(int id, Dish dish, User user, int quantity, double price, double sum) {
        this.id = id;
        this.dish = dish;
        this.user = user;
        this.quantity = quantity;
        this.price = price;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItems)) return false;

        OrderItems that = (OrderItems) o;

        if (getId() != that.getId()) return false;
        if (getQuantity() != that.getQuantity()) return false;
        if (Double.compare(that.getPrice(), getPrice()) != 0) return false;
        if (Double.compare(that.getSum(), getSum()) != 0) return false;
        if (getDish() != null ? !getDish().equals(that.getDish()) : that.getDish() != null) return false;
        return getUser() != null ? getUser().equals(that.getUser()) : that.getUser() == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId();
        result = 31 * result + (getDish() != null ? getDish().hashCode() : 0);
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        result = 31 * result + getQuantity();
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSum());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "OrderItems{" +
                "id=" + id +
                ", dish=" + dish +
                ", user=" + user +
                ", quantity=" + quantity +
                ", price=" + price +
                ", sum=" + sum +
                '}';
    }
}
