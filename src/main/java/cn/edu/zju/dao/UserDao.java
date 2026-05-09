package cn.edu.zju.dao;

import cn.edu.zju.bean.User;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Properties;

public class UserDao {
    private final String jdbcUrl;
    private final String jdbcUser;
    private final String jdbcPassword;

    public UserDao() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("app.properties")) {
            if (in == null) {
                throw new RuntimeException("无法找到配置文件 app.properties 在 classpath");
            }
            Properties p = new Properties();
            p.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            jdbcUrl = p.getProperty("jdbc.url");
            jdbcUser = p.getProperty("jdbc.username");
            jdbcPassword = p.getProperty("jdbc.password");
            if (jdbcUrl == null) throw new RuntimeException("jdbc.url 未配置");
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

    public boolean exists(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
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

    public boolean verifyPassword(String username, String password) throws SQLException {
        User u = findByUsername(username);
        if (u == null) return false;
        String hash = hashPassword(password);
        return hash.equals(u.getPasswordHash());
    }

    public void updatePassword(long userId, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        try (Connection c = getConn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, hashPassword(newPassword));
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bs = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bs) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
