package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    @Autowired
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        String sql = """
                SELECT * FROM categories""";


        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Category category = mapRow(resultSet);
                    categories.add(category);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        String sql = """
                SELECT * FROM categories WHERE category_id = ?""";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, categoryId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Category category = mapRow(resultSet);
                    return category;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // get category by id
        return null;
    }

    @Override
    public Category create(Category category) {
        String sql = """
                INSERT INTO categories (name, description) VALUES (?,?)""";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Insert failed, no rows affected!");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                int generatedId = generatedKeys.getInt(1);
                category.setCategoryId(generatedId);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // create a new category
        return category;
    }

    @Override
    public void update(int categoryId, Category category) {
        String sql = """
                UPDATE categories SET name = ?, description = ? WHERE category_id = ?""";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Update failed, there is no such id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId) {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }


}
