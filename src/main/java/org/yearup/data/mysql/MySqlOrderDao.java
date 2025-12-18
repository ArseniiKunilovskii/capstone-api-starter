package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.*;
import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {
    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order create(Order order, ShoppingCart cart) {
        String orderSql = "INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String lineItemSql = "INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false); // Enable transaction [cite: 321]

            try (PreparedStatement statement = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, order.getUserId());
                statement.setTimestamp(2, Timestamp.valueOf(order.getDate()));
                statement.setString(3, order.getAddress());
                statement.setString(4, order.getCity());
                statement.setString(5, order.getState());
                statement.setString(6, order.getZip());
                statement.setBigDecimal(7, order.getShippingAmount());
                statement.executeUpdate();

                ResultSet keys = statement.getGeneratedKeys();
                if (keys.next()) { order.setOrderId(keys.getInt(1)); }
            }

            try (PreparedStatement statement = connection.prepareStatement(lineItemSql)) {
                for (ShoppingCartItem item : cart.getItems().values()) {
                    statement.setInt(1, order.getOrderId());
                    statement.setInt(2, item.getProductId());
                    statement.setBigDecimal(3, item.getProduct().getPrice());
                    statement.setInt(4, item.getQuantity());
                    statement.setBigDecimal(5, item.getDiscountPercent());
                    statement.addBatch();
                }
                statement.executeBatch();
            }

            connection.commit();
            return order;
        } catch (SQLException e) {
            throw new RuntimeException("Transaction failed: Checkout could not be completed.", e);
        }
    }
}