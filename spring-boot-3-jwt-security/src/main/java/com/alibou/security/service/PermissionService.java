package com.alibou.security.service;

import com.alibou.security.domain.Permission;
import com.alibou.security.domain.User;
import com.alibou.security.domain.UserPermission;
import com.alibou.security.repository.PermissionRepository;
import com.alibou.security.repository.UserPermissionRepository;
import com.alibou.security.repository.UserRepository;
import com.alibou.security.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Optional<Permission> findById(UUID id) {
        return permissionRepository.findById(id);
    }

    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    public void deleteById(UUID id) {
        permissionRepository.deleteById(id);
    }

    public Permission update(UUID id, Permission permissionDetails) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));
        permission.setName(permissionDetails.getName());
        permission.setPath(permissionDetails.getPath());
        permission.setParentId(permissionDetails.getParentId());
        return permissionRepository.save(permission);
    }

    public void addPermissionToUser(UUID userId, UUID permissionId) {
        UserPermission userPermission=UserPermission.builder().permissionId(permissionId).userId(userId).build();
        userPermissionRepository.save(userPermission);
    }

    public void removePermissionFromUser(UUID userId, UUID permissionId) {

        List<UserPermission> userPermissions= userPermissionRepository.findByUserIdAndPermissionId(userId,permissionId);
        userPermissionRepository.deleteAll(userPermissions);
    }
}
