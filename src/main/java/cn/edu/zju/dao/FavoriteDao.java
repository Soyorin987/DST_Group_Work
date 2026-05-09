package cn.edu.zju.dao;

import cn.edu.zju.bean.Drug;
import cn.edu.zju.bean.Favorite;
import cn.edu.zju.dbutils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDao {

    private Connection getConn() throws SQLException {
        Connection connection = DBUtils.getConnection();

        if (connection == null) {
            throw new SQLException("Failed to get database connection from DBUtils.");
        }

        return connection;
    }

    public void save(Favorite f) throws SQLException {
        String sql = "INSERT IGNORE INTO favorites (user_id, resource_type, resource_id) VALUES (?, ?, ?)";

        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, f.getUserId());
            ps.setString(2, f.getResourceType());
            ps.setString(3, f.getResourceId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    f.setId(rs.getLong(1));
                }
            }
        }
    }

    public boolean exists(long userId, String resourceType, String resourceId) throws SQLException {
        String sql = "SELECT 1 FROM favorites WHERE user_id = ? AND resource_type = ? AND resource_id = ? LIMIT 1";

        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setString(2, resourceType);
            ps.setString(3, resourceId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void deleteByUserAndResource(long userId, String resourceType, String resourceId) throws SQLException {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND resource_type = ? AND resource_id = ?";

        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setString(2, resourceType);
            ps.setString(3, resourceId);
            ps.executeUpdate();
        }
    }

    public List<String> findFavoriteResourceIds(long userId, String resourceType) throws SQLException {
        String sql = "SELECT resource_id FROM favorites WHERE user_id = ? AND resource_type = ?";

        List<String> ids = new ArrayList<>();

        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setString(2, resourceType);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getString("resource_id"));
                }
            }
        }

        return ids;
    }

    public List<Drug> findFavoriteDrugsByUserId(long userId) throws SQLException {
        String sql =
                "SELECT d.id, d.name, d.obj_cls, d.drug_url, d.biomarker " +
                        "FROM favorites f " +
                        "JOIN drug d ON TRIM(f.resource_id) = TRIM(d.id) " +
                        "WHERE f.user_id = ? AND f.resource_type = 'drug' " +
                        "ORDER BY f.created_at DESC";

        List<Drug> drugs = new ArrayList<>();

        try (Connection c = getConn();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Drug drug = new Drug(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getBoolean("biomarker"),
                            rs.getString("drug_url"),
                            rs.getString("obj_cls")
                    );

                    drug.setFavorited(true);
                    drugs.add(drug);
                }
            }
        }

        return drugs;
    }
}