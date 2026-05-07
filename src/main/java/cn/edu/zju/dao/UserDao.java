// java
package cn.edu.zju.dao;

import cn.edu.zju.bean.User;

import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Properties;

public class UserDao {
    private String jdbcUrl;
    private String jdbcUser;
    private String jdbcPassword;

    public UserDao() {
        try {
            Properties p = new Properties();
            InputStream in = getClass().getClassLoader().getResourceAsStream("app.properties");
            p.load(in);
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

    public void save(User u) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, authorization) VALUES (?, ?, ?)";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getAuthorization());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getLong(1));
            }
        }
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password_hash, authorization, created_at FROM users WHERE username = ? LIMIT 1";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getLong("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setAuthorization(rs.getString("authorization"));
                    u.setCreatedAt(rs.getTimestamp("created_at"));
                    return u;
                }
            }
        }
        return null;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bs = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bs) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
