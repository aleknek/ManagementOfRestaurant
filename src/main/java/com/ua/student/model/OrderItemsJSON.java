package com.ua.student.model;

public class OrderItemsJSON {

    private double sum;
    private double sumTotal;
    private double sumTotalOfUser;

    public OrderItemsJSON() {
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(double sumTotal) {
        this.sumTotal = sumTotal;
    }

    public double getSumTotalOfUser() {
        return sumTotalOfUser;
    }

    public void setSumTotalOfUser(double sumTotalOfUser) {
        this.sumTotalOfUser = sumTotalOfUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemsJSON)) return false;

        OrderItemsJSON that = (OrderItemsJSON) o;

        if (Double.compare(that.getSum(), getSum()) != 0) return false;
        if (Double.compare(that.getSumTotal(), getSumTotal()) != 0) return false;
        return Double.compare(that.getSumTotalOfUser(), getSumTotalOfUser()) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getSum());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSumTotal());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getSumTotalOfUser());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
