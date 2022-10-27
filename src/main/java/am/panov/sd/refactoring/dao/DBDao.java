package am.panov.sd.refactoring.dao;

import am.panov.sd.refactoring.exception.DaoException;
import am.panov.sd.refactoring.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DBDao {
    private final String connectionUrl;

    public DBDao(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public void insert(final Product product) throws SQLException {
        String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + product.name + "\"," + product.price + ")";

        executeUpdate(sql);
    }

    public List<Product> selectAll() throws SQLException {
        String sql = "SELECT * FROM PRODUCT";

        return executeQuery(sql, this::parseProducts);
    }

    public Optional<Product> max() throws SQLException {
        String sql = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";

        return limitOne(executeQuery(sql, this::parseProducts));
    }

    public Optional<Product> min() throws SQLException {
        String sql = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";

        return limitOne(executeQuery(sql, this::parseProducts));
    }

    public int sum() throws SQLException {
        String sql = "SELECT SUM(price) FROM PRODUCT";

        return executeQuery(sql, this::getFirst);
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM PRODUCT";

        return executeQuery(sql, this::getFirst);
    }

    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";

        executeUpdate(sql);
    }

    private Optional<Product> limitOne(List<Product> products) {
         return products.isEmpty() ? Optional.empty() : Optional.of(products.get(0));
    }

    private int getFirst(ResultSet rs) {
        try {
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private <T> T executeQuery(String sqlQuery, Function<ResultSet, T> handleResult) throws SQLException {
        T result;

        try (Connection c = DriverManager.getConnection(connectionUrl)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);

            result = handleResult.apply(rs);

            rs.close();
            stmt.close();
        }

        return result;
    }

    private void executeUpdate(String sqlQuery) throws SQLException {
        try (Connection c = DriverManager.getConnection(connectionUrl)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sqlQuery);

            stmt.close();
        }
    }

    private List<Product> parseProducts(ResultSet rs) throws DaoException {
        List<Product> products = new ArrayList<>();

        try {
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                products.add(new Product(name, price));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        return products;
    }
}
