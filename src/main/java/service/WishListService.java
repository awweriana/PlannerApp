package service;

import model.WishItem;
import utils.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishListService {
    public void saveWishListService(WishItem wishItem) {

        String sql ="INSERT INTO WishList (itemName, price, path) VALUES (?, ?, ?)";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // устанавливаем параметры в sql запросе
            stmt.setString(1, wishItem.getItemName());
            stmt.setDouble(2, wishItem.getPrice());
            stmt.setString(3, wishItem.getPath());

            stmt.executeUpdate();// сохраняем задачи в БД

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<WishItem> loadWishListService() {
        List<WishItem> items = new ArrayList<>();
        String sql = "SELECT itemName, price, path FROM WishList";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("itemName");
                double price = rs.getDouble("price");
                String path = rs.getString("path");

                items.add(new WishItem(name, price, path));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
    public void deleteWishList(WishItem wishItem) {
        String sql = "DELETE FROM WishList WHERE itemName = ? AND price = ? ";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement rs = conn.prepareStatement(sql)) {

            rs.setString(1, wishItem.getItemName());
            rs.setDouble(2, wishItem.getPrice());

            rs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
