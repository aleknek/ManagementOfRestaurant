package com.ua.student.model;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.Serializable;
import java.util.Arrays;

public class Dish implements Serializable {

    private int id;
    private String name;
    private String description;
    private byte[] image;
    private TypeDishes typeDishes;
    private double price;
    private CommonsMultipartFile fileData;

    public Dish() {
    }

    public Dish(int id, String name, String description, byte[] image, TypeDishes typeDishes, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.typeDishes = typeDishes;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeDishes getTypeDishes() {
        return typeDishes;
    }

    public void setTypeDishes(TypeDishes typeDishes) {
        this.typeDishes = typeDishes;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public CommonsMultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(CommonsMultipartFile fileData) {
        this.fileData = fileData;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dish)) return false;

        Dish dish = (Dish) o;

        if (getId() != dish.getId()) return false;
        if (Double.compare(dish.getPrice(), getPrice()) != 0) return false;
        if (getName() != null ? !getName().equals(dish.getName()) : dish.getName() != null) return false;
        if (getDescription() != null ? !getDescription().equals(dish.getDescription()) : dish.getDescription() != null)
            return false;
        return getTypeDishes() == dish.getTypeDishes();

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getTypeDishes() != null ? getTypeDishes().hashCode() : 0);
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image=" + Arrays.toString(image) +
                ", typeDishes=" + typeDishes +
                ", price=" + price +
                ", fileData=" + fileData +
                '}';
    }
}

