package com.alibou.security.service;

import com.alibou.security.domain.Role;
import com.alibou.security.domain.UserRole;
import com.alibou.security.repository.RoleRepository;
import com.alibou.security.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public Role addRole(Role role) {
       return  repository.save(role);
    }

    public List<Role> getAll() {
        return  repository.findAll();
    }


    public UserRole addRoleWithUser(UUID idRole,UUID idUser) {
        return  userRoleRepository.save(UserRole.builder().roleId(idRole).userId(idUser).build());
    }

    public Role save(Role role){
        return repository.save(role);
    }

}
