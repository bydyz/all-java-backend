package org.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // 方法名自动推导查询：WHERE email = ?
    User findByEmail(String email);

    // 模糊查询：WHERE username LIKE %?%
    List<User> findByUsernameContaining(String keyword);
}
