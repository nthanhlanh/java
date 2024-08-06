package com.alibou.security.repository;

import java.util.List;
import java.util.Optional;

import com.alibou.security.domain.Role;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alibou.security.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  @Query(value = "SELECT r.name FROM _role r " +
          "INNER JOIN _user_roles ur ON r.id = ur.role_id " +
          "WHERE ur.user_id = :userId", nativeQuery = true)
  List<String> findRolesByUserId(@Param("userId") Integer userId);

}
