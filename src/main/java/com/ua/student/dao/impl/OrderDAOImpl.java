package com.ua.student.dao.impl;

import com.ua.student.dao.DishDAO;
import com.ua.student.dao.OrderDAO;
import com.ua.student.dao.UserDAO;
import com.ua.student.dao.UserGroupsDAO;
import com.ua.student.model.*;
import com.ua.student.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Autowired
    private DishDAO dishDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserGroupsDAO userGroupsDAO;

    @Autowired
    private OrderService orderService;

    private JdbcTemplate jdbcTemplate;

    public OrderDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(Order order, int numberOfGroup, OrderStatuses orderStatuses) {

        String sqlOrder = "INSERT INTO orders (dateOfOrder, numberOfOrder, id_userGroup, amount, id_orderStatus)"
                + " VALUES (?, ?, ?, ?, ?)";

        order.setNumberOfOrder(getNewNumberOfOrder() + 1);

        jdbcTemplate.update(sqlOrder, new Date(), order.getNumberOfOrder(), numberOfGroup, orderService.getSumTotal(order.getOrderItems()),
                orderStatuses.ordinal() + 1);

        String sqlOrderItems = "INSERT INTO order_items (numberOfOrder, id_user, id_dish, quantity, amount, price)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        for (OrderItems item : order.getOrderItems()) {
            jdbcTemplate.update(sqlOrderItems, order.getNumberOfOrder(), item.getUser().getId(), item.getDish().getId(), item.getQuantity(),
                    item.getSum(), item.getPrice());
        }
    }

    @Override
    public void update(Order order, OrderStatuses orderStatuses) {

        String sqlOrder = "UPDATE orders SET amount = ?, id_orderStatus = ? WHERE numberOfOrder= ?";

        jdbcTemplate.update(sqlOrder, order.getSum(), orderStatuses.ordinal() + 1, order.getNumberOfOrder());


        String sqlOrderItems = "INSERT INTO order_items (numberOfOrder, id_user, id_dish, quantity, amount, price)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update("DELETE FROM order_items WHERE numberOfOrder = " + order.getNumberOfOrder());

        List<OrderItems> orderItems = order.getOrderItems();

        for (OrderItems item : orderItems) {
            if (item.getQuantity() == 0) {
                continue;
            }
            jdbcTemplate.update(sqlOrderItems, order.getNumberOfOrder(), item.getUser().getId(), item.getDish().getId(), item.getQuantity(),
                    item.getDish().getPrice() * item.getQuantity(), item.getDish().getPrice());
        }
    }


    @Override
    public Order getOrderByNumberOfOrder(int numberOfOrder) {

        String sql = "SELECT orders.id AS id, orders.dateOfOrder, orders.numberOfOrder, orders.id_userGroup, orders.amount, " +
                "order_statuses.name AS orderStatus " +
                "FROM db_restaurant.orders AS orders " +
                "LEFT JOIN db_restaurant.order_statuses AS order_statuses " +
                "ON orders.id_orderStatus = order_statuses.id " +
                "WHERE orders.numberOfOrder = " + numberOfOrder + " " +
                "ORDER BY orders.numberOfOrder DESC";

        return rowMapperQueryForObject(sql);
    }

    @Override
    public List<Order> getListOrders() {
        String sql = "SELECT orders.id, orders.dateOfOrder, orders.numberOfOrder, orders.id_userGroup, orders.amount, " +
                "order_statuses.name AS orderStatus " +
                "FROM db_restaurant.orders AS orders " +
                "LEFT JOIN db_restaurant.order_statuses AS order_statuses " +
                "ON orders.id_orderStatus = order_statuses.id " +
                "ORDER BY orders.id DESC ";
        return rowMapperQuery(sql);
    }

    @Override
    public List<Order> getListMyOrders(User user) {
        String sql = "SELECT orders.id, orders.dateOfOrder, orders.numberOfOrder, orders.id_userGroup, orders.amount, " +
                "order_statuses.name AS orderStatus FROM db_restaurant.orders AS orders " +
                "LEFT JOIN db_restaurant.order_statuses AS order_statuses ON orders.id_orderStatus = order_statuses.id " +
                "WHERE numberOfOrder IN " +
                "(SELECT numberOfOrder FROM db_restaurant.order_items where id_user = " + user.getId() + " " +
                "GROUP BY numberOfOrder) " +
                "OR numberOfOrder IN " +
                "(SELECT orders.numberOfOrder FROM db_restaurant.orders AS orders " +
                "WHERE id_userGroup " +
                "IN (SELECT group_members_1.numberOfGroup AS id_userGroup " +
                "FROM db_restaurant.group_members AS group_members " +
                "LEFT JOIN db_restaurant.group_members AS group_members_1 " +
                "ON group_members.numberOfGroup = group_members_1.numberOfGroup " +
                "WHERE group_members.id_user = " + user.getId() + " " +
                "GROUP BY id_userGroup) " +
                "GROUP BY numberOfOrder) " +
                "ORDER BY orders.id DESC;";
        return rowMapperQuery(sql);
    }

    public List<OrderItems> getOrderItemsByNumberOfOrder(int numberOfOrder) {

        String sql = "SELECT order_items.id_dish, order_items.quantity, order_items.amount, " +
                "order_items.price, order_items.id, order_items.id_user FROM db_restaurant.order_items AS order_items " +
                "WHERE order_items.numberOfOrder = " + numberOfOrder;

        return jdbcTemplate.query(sql, new RowMapper<OrderItems>() {
            @Override
            public OrderItems mapRow(ResultSet resultSet, int i) throws SQLException {
                OrderItems orderItems = new OrderItems();
                orderItems.setId(resultSet.getInt("id"));
                orderItems.setSum(resultSet.getDouble("amount"));
                orderItems.setQuantity(resultSet.getInt("quantity"));
                orderItems.setPrice(resultSet.getDouble("price"));
                orderItems.setUser(userDAO.getUserById(resultSet.getInt("id_user")));
                orderItems.setDish(dishDAO.getDishById(resultSet.getInt("id_dish")));
                return orderItems;
            }
        });
    }

    public int getNewNumberOfOrder() {
        String sql = "SELECT IFNULL(max(numberOfOrder),0) AS numberOfOrder FROM db_restaurant.orders ";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public int isOrderSent(int numberOfOrder) {
        String sql = "SELECT IFNULL(MAX(orders.id_orderStatus), 0) AS id_orderStatus FROM db_restaurant.orders AS orders " +
                "WHERE orders.id_orderStatus = " + 2 + " AND orders.numberOfOrder = " + numberOfOrder;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public int checkAvailabilityGroupOrder(int numberOfOrder, int idUser) {
        String sql = "SELECT IFNULL(MAX(orders.numberOfOrder), 0) AS numberOfOrder FROM db_restaurant.orders AS orders " +
                "WHERE numberOfOrder = " + numberOfOrder + " AND orders.id_userGroup IN (SELECT group_members.numberOfGroup AS numberOfGroup " +
                "FROM db_restaurant.group_members AS group_members " +
                "WHERE group_members.id_user = " + idUser + " ) ";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Order rowMapperQueryForObject(String sql) {
        try {
            return jdbcTemplate.queryForObject(sql, new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet resultSet, int i) throws SQLException {
                    if (resultSet.getInt("id_userGroup") == 0) {
                        return OrderRowMapper(resultSet);
                    }
                    return OrderGroupRowMapper(resultSet);
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Order> rowMapperQuery(String sql) {
        try {
            return jdbcTemplate.query(sql, new RowMapper<Order>() {
                @Override
                public Order mapRow(ResultSet resultSet, int i) throws SQLException {
                    if (resultSet.getInt("id_userGroup") == 0) {
                        return OrderRowMapper(resultSet);
                    }
                    return OrderGroupRowMapper(resultSet);
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Order OrderRowMapper(ResultSet resultSet) throws SQLException {
        return fillOrderByResultSet(new Order(), resultSet);
    }

    public Order OrderGroupRowMapper(ResultSet resultSet) throws SQLException {
        OrderGroup order = new OrderGroup();
        order.setUserGroup(userGroupsDAO.getUserGroupByNumberOfGroup(resultSet.getInt("id_userGroup")));
        return fillOrderByResultSet(order, resultSet);
    }

    public Order fillOrderByResultSet(Order order, ResultSet resultSet) throws SQLException {
        order.setId(resultSet.getInt("id"));
        order.setDateOfOrder(resultSet.getDate("dateOfOrder"));
        order.setNumberOfOrder(resultSet.getInt("numberOfOrder"));
        order.setSum(resultSet.getDouble("amount"));
        order.setOrderStatus(OrderStatuses.valueOf(resultSet.getString("orderStatus")));
        return order;
    }
}
