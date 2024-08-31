package com.alibou.security.repository;

import com.alibou.security.domain.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, UUID> {

    List<UserPermission> findByUserId(UUID userId);

    List<UserPermission> findByUserIdAndPermissionId(UUID userId,UUID permissionId);
}
