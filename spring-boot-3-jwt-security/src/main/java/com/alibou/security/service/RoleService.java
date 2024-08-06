package com.alibou.security.service;

import com.alibou.security.domain.Role;
import com.alibou.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    public Role addRole(Role role) {
       return  repository.save(role);
    }
}
