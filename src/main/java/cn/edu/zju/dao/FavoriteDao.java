package cn.edu.zju.dao;

import cn.edu.zju.bean.Favorite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FavoriteDao {
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;

    public FavoriteDao() {
        try {
            Properties p = new Properties();
            p.load(getClass().getClassLoader().getResourceAsStream("app.properties"));
            jdbcUrl = p.getProperty("jdbc.url");
            jdbcUser = p.getProperty("jdbc.username");
            jdbcPassword = p.getProperty("jdbc.password");
        } catch (Exception e) {
            throw new RuntimeException("加载数据库配置失败", e);
        }
    }

    private Connection getConn() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
    }

    public void save(Favorite f) throws SQLException {
        String sql = "INSERT INTO favorites (user_id, resource_type, resource_id) VALUES (?, ?, ?)";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, f.getUserId());
            ps.setString(2, f.getResourceType());
            ps.setLong(3, f.getResourceId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) f.setId(rs.getLong(1));
            }
        }
    }

    public boolean exists(long userId, String resourceType, long resourceId) throws SQLException {
        String sql = "SELECT 1 FROM favorites WHERE user_id=? AND resource_type=? AND resource_id=? LIMIT 1";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setString(2, resourceType);
            ps.setLong(3, resourceId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void deleteByUserAndResource(long userId, String resourceType, long resourceId) throws SQLException {
        String sql = "DELETE FROM favorites WHERE user_id=? AND resource_type=? AND resource_id=?";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setString(2, resourceType);
            ps.setLong(3, resourceId);
            ps.executeUpdate();
        }
    }

    public List<Favorite> findByUserId(long userId) throws SQLException {
        String sql = "SELECT id, user_id, resource_type, resource_id, created_at FROM favorites WHERE user_id=? ORDER BY created_at DESC";
        List<Favorite> list = new ArrayList<>();
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Favorite f = new Favorite();
                    f.setId(rs.getLong("id"));
                    f.setUserId(rs.getLong("user_id"));
                    f.setResourceType(rs.getString("resource_type"));
                    f.setResourceId(rs.getLong("resource_id"));
                    f.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(f);
                }
            }
        }
        return list;
    }
}
