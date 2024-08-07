package com.alibou.security.controller;

import com.alibou.security.domain.Permission;
import com.alibou.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/permissions")
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Optional<Permission> permission = permissionService.findById(id);
        return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Permission createPermission(@RequestBody Permission permission) {
        return permissionService.save(permission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permissionDetails) {
        try {
            Permission updatedPermission = permissionService.update(id, permissionDetails);
            return ResponseEntity.ok(updatedPermission);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        try {
            permissionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user/{userId}/add/{permissionId}")
    public ResponseEntity<Void> addPermissionToUser(@PathVariable Integer userId, @PathVariable Long permissionId) {
        try {
            permissionService.addPermissionToUser(userId, permissionId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/user/{userId}/remove/{permissionId}")
    public ResponseEntity<Void> removePermissionFromUser(@PathVariable Integer userId, @PathVariable Long permissionId) {
        try {
            permissionService.removePermissionFromUser(userId, permissionId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
