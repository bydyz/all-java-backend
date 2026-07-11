package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问层
 * 
 * 继承 JpaRepository 提供标准 CRUD 操作
 * JpaRepository<T, ID> - T 为实体类型，ID 为主键类型
 * 
 * Spring Data JPA 会根据方法名自动生成查询
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 方法名查询（Query Derivation）
     * Spring Data JPA 根据方法名自动生成 SQL
     * findByLastName -> SELECT * FROM user WHERE last_name = ?
     */
    List<User> findByName(String name);

    /**
     * 方法名查询 - 忽略大小写
     */
    List<User> findByNameContainingIgnoreCase(String name);

    /**
     * 方法名查询 - 多条件
     */
    List<User> findByStatus(User.UserStatus status);

    /**
     * @Query 注解 - 自定义 JPQL 查询
     * JPQL 使用实体类名和字段名，不是表名和列名
     */
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * @Query - 原生 SQL 查询
     * nativeQuery = true 表示使用原生 SQL
     */
    @Query(value = "SELECT * FROM users WHERE name LIKE %:keyword% OR email LIKE %:keyword%", 
           nativeQuery = true)
    List<User> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 检查是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 方法名查询 - 分页排序
     * 返回 Page 对象包含分页信息
     */
    // Page<User> findByStatus(User.UserStatus status, Pageable pageable);

    /**
     * 批量删除
     */
    void deleteByStatus(User.UserStatus status);
}
