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
import java.util.HashMap;
import java.util.Map;

@Component
public class MySqlShoppingCardDao extends  MySqlDaoBase implements ShoppingCartDao {
    private ProductDao productDao;

    @Autowired
    public MySqlShoppingCardDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        String sql = """
                SELECT * FROM shopping_cart WHERE userId = ?""";

        Map<Integer, ShoppingCartItem> items = new HashMap<>();

        int counter = 1;



        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, userId);

            try(ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()){
                    ShoppingCartItem item = new ShoppingCartItem();
                    Product product = productDao.getById(resultSet.getInt(2));
                    int quantity = resultSet.getInt(3);
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    items.put(counter, item);
                    counter++;
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return shoppingCart;
    }
}
