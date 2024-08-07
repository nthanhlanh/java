package com.alibou.security.repository;

import com.alibou.security.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query(value = "SELECT r.path FROM permissions r " +
            "INNER JOIN user_permissions ur ON r.id = ur.permission_id " +
            "WHERE ur.user_id = :userId", nativeQuery = true)
    List<String> findPermissionsByUserId(@Param("userId") Integer userId);

    @Query(value = "SELECT r.path FROM permissions r " +
            "INNER JOIN user_permissions ur ON r.id = ur.permission_id " +
            "INNER JOIN _user u ON ur.user_id = u.id " +
            "WHERE u.email = :email", nativeQuery = true)
    List<String> findPermissionsByEmail(@Param("email") String email);

}
