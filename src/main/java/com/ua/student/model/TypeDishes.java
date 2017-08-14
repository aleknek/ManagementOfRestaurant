package com.ua.student.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public enum TypeDishes implements Serializable{

    NONE("--- Select ---"),
    firstDishes("Первые блюда"),
    secondDishes("Вторые блюда"),
    salads("Салаты"),
    deserts("Десерты"),
    drinks("Напитки");

    private String name;

    TypeDishes(String name) {
        this.name = name;
    }

    TypeDishes() {
    }

    public String getName() {
        return name;
    }

    public static List<String> getTypeDishes() {

        List<String> list = new ArrayList<>();

        TypeDishes[] typeDishes = TypeDishes.values();
        for (TypeDishes currentDish : typeDishes) {
            list.add(currentDish.getName());
        }
        return list;
    }
}
