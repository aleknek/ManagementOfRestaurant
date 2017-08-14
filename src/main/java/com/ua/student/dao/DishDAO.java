package com.ua.student.dao;

import com.ua.student.model.Dish;
import com.ua.student.model.TypeDishes;

import java.util.List;

public interface DishDAO {

    void insert(Dish dish);

    void update(Dish dish);

    void delete(int id);

    Dish getDishById(int id);

    List<Dish> getDishListByType(TypeDishes typeOfDishes);

}
