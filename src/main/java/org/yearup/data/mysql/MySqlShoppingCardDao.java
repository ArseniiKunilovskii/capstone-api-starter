package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MySqlShoppingCardDao extends  MySqlDaoBase implements ShoppingCartDao {
    private ProductDao productDao;

    @Autowired
    public MySqlShoppingCardDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        String sql = """
                SELECT * FROM shopping_cart WHERE user_id = ?""";

        Map<Integer, ShoppingCartItem> items = new HashMap<>();

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, userId);

            try(ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()){
                    int productId = resultSet.getInt(2);
                    ShoppingCartItem item = new ShoppingCartItem();
                    Product product = productDao.getById(productId);
                    int quantity = resultSet.getInt(3);
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    items.put(productId, item);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return shoppingCart;
    }

    public void addItemToCart(int id, ShoppingCartItem item){
        String sql = """
                INSERT INTO shopping_cart (user_id,product_id,quantity) VALUES (?,?,?)""";

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setInt(2, item.getProductId());
            statement.setInt(3, item.getQuantity());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void increaseQuantityByOne(int userId, int productId){
        String sql = """
                UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id = ? AND product_id = ?""";

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearCart(int userId){
        String sql= """
                DELETE FROM shopping_cart where user_id = ?""";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, userId);

            statement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
