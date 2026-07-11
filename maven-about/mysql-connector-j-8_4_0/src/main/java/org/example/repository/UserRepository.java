package org.example.repository;

import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper 将数据库行映射为 User 对象
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            user.setCreatedAt(timestamp.toLocalDateTime());
        }
        return user;
    };

    /**
     * 创建用户
     */
    public User save(User user) {
        String sql = "INSERT INTO users (username, email) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            user.setId(key.longValue());
        }
        logger.info("用户创建成功: {}", user);
        return user;
    }

    /**
     * 根据 ID 查找用户
     */
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, id);
        return users.stream().findFirst();
    }

    /**
     * 查找所有用户
     */
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    /**
     * 更新用户
     */
    public User update(User user) {
        String sql = "UPDATE users SET username = ?, email = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getId());
        if (rowsAffected > 0) {
            logger.info("用户更新成功: {}", user);
        } else {
            logger.warn("用户未找到，ID: {}", user.getId());
        }
        return user;
    }

    /**
     * 删除用户
     */
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected > 0) {
            logger.info("用户删除成功，ID: {}", id);
            return true;
        } else {
            logger.warn("用户未找到，ID: {}", id);
            return false;
        }
    }

    /**
     * 根据用户名查找用户
     */
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, username);
        return users.stream().findFirst();
    }

    /**
     * 检查用户是否存在
     */
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    /**
     * 获取用户数量
     */
    public long count() {
        String sql = "SELECT COUNT(*) FROM users";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
}