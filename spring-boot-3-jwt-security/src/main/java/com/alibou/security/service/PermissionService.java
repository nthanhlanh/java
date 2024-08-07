package com.alibou.security.service;

import com.alibou.security.domain.Permission;
import com.alibou.security.domain.User;
import com.alibou.security.repository.PermissionRepository;
import com.alibou.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    public Permission update(Long id, Permission permissionDetails) {
        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission not found"));
        permission.setName(permissionDetails.getName());
        permission.setPath(permissionDetails.getPath());
        permission.setParentId(permissionDetails.getParentId());
        return permissionRepository.save(permission);
    }

    public void addPermissionToUser(Integer userId, Long permissionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));
        user.getPermissions().add(permission);
        userRepository.save(user);
    }

    public void removePermissionFromUser(Integer userId, Long permissionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));
        user.getPermissions().remove(permission);
        userRepository.save(user);
    }
}
