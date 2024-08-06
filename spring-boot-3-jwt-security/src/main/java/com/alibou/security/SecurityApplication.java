package com.alibou.security;

import com.alibou.security.domain.Role;
import com.alibou.security.dto.RegisterRequest;
import com.alibou.security.service.AuthenticationService;

import com.alibou.security.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.HashSet;
import java.util.Set;

import static com.alibou.security.dto.RoleType.ADMIN;
import static com.alibou.security.dto.RoleType.MANAGER;
import static com.alibou.security.dto.RoleType.USER;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService service,
//			RoleService roleService
//	) {
//		Set<Role> roleAdmins = new HashSet<>();
//		Set<Role> roleManagers = new HashSet<>();
//		roleAdmins.add(roleService.addRole(new Role(1,ADMIN,null)));
//		roleManagers.add(roleService.addRole(new Role(2,MANAGER,null)));
//		roleManagers.add(roleService.addRole(new Role(3,USER,null)));
//
//		return args -> {
//			var admin = RegisterRequest.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("admin@mail.com")
//					.password("password")
//					.roles(roleAdmins)
//					.build();
//
//			System.out.println("Admin token: " + service.register(admin).getAccessToken());
//
//			var manager = RegisterRequest.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("manager@mail.com")
//					.password("password")
//					.roles(roleManagers)
//					.build();
//			System.out.println("Manager token: " + service.register(manager).getAccessToken());
//
//		};
//	}
}
