package com.ua.student.model;

import java.io.Serializable;

public enum OrderStatuses implements Serializable{

    newOrder("Новый"),
    closed("Отправлен");

    private String name;

    OrderStatuses() {
    }

    OrderStatuses(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

