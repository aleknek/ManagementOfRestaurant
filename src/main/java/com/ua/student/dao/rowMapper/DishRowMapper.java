package com.ua.student.dao.rowMapper;

import com.ua.student.model.Dish;
import com.ua.student.model.TypeDishes;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DishRowMapper implements RowMapper<Dish> {

    public Dish mapRow(ResultSet resultSet, int i) throws SQLException {

        Dish dish = new Dish();
        dish.setId(resultSet.getInt("id"));
        dish.setName(resultSet.getString("name"));
        dish.setPrice(resultSet.getDouble("price"));
        dish.setImage(resultSet.getBytes("image"));
        dish.setTypeDishes(TypeDishes.valueOf(resultSet.getString("typeOfDish")));
        dish.setDescription(resultSet.getString("description"));

        return dish;
    }
}
