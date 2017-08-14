package com.ua.student.dao.impl;

import com.ua.student.dao.DishDAO;
import com.ua.student.dao.rowMapper.DishRowMapper;
import com.ua.student.model.Dish;
import com.ua.student.model.TypeDishes;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class DishDAOImpl implements DishDAO {

    private static final DishRowMapper DISH_ROW_MAPPER = new DishRowMapper();

    private JdbcTemplate jdbcTemplate;

    public DishDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(Dish dish) {
        String sql = "INSERT INTO dishes (name, description, id_typeDishes, price, image, active)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, dish.getName(), dish.getDescription(),
                dish.getTypeDishes().ordinal() + 1, String.valueOf(dish.getPrice()), dish.getImage(), 1);
    }

    public void update(Dish dish) {

        String sql = "UPDATE dishes SET name=?, description=?, id_typeDishes=?, price=?, image=?  WHERE id=?";
        jdbcTemplate.update(sql, dish.getName(), dish.getDescription(),
                dish.getTypeDishes().ordinal() + 1, String.valueOf(dish.getPrice()), dish.getImage(), String.valueOf(dish.getId()));
    }

    public void delete(int id) {
        String sql = "UPDATE dishes SET active = 0 WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public Dish getDishById(int id) {
        String sql = "SELECT dishes.id, dishes.name, dishes.description, dishes.price, dishes.image, 0 AS quantity, " +
                "type_dishes.name AS typeOfDish FROM db_restaurant.dishes AS dishes " +
                "LEFT JOIN db_restaurant.type_dishes AS type_dishes " +
                "ON id_typeDishes = type_dishes.id " +
                "WHERE dishes.id = " + id;
        return jdbcTemplate.queryForObject(sql, DISH_ROW_MAPPER);
    }

    @Override
    public List<Dish> getDishListByType(TypeDishes typeOfDishes) {
        String sql;
        sql = " SELECT dishes.id, dishes.name, dishes.description, dishes.price, dishes.image, 0 AS quantity, " +
                "type_dishes.name AS typeOfDish FROM db_restaurant.dishes AS dishes " +
                "LEFT JOIN db_restaurant.type_dishes AS type_dishes " +
                "ON id_typeDishes = type_dishes.id " +
                "WHERE type_dishes.id =  " + (typeOfDishes.ordinal() + 1) + " AND dishes.active = 1 " + "ORDER BY name";
        return jdbcTemplate.query(sql, DISH_ROW_MAPPER);
    }
}
