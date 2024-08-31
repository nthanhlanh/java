package com.alibou.security;

import com.alibou.security.domain.Permission;
import com.alibou.security.domain.Role;
import com.alibou.security.domain.User;
import com.alibou.security.domain.UserPermission;
import com.alibou.security.dto.RegisterRequest;
import com.alibou.security.service.AuthenticationService;

import com.alibou.security.service.PermissionService;
import com.alibou.security.service.RoleService;
import com.alibou.security.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
//			PasswordEncoder passwordEncoder,
//			UserService userService,
//			RoleService roleService,
//			PermissionService permissionService
//	) {
//
//		return args -> {
//			//user
//			var admin = User.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("admin@mail.com")
//					.password(passwordEncoder.encode("password"))
//					.build();
//			var saveAdmin= userService.save(admin);
//
//			var manager = User.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("manager@mail.com")
//					.password(passwordEncoder.encode("password"))
//					.build();
//			var saveManager= userService.save(manager);
//			//role
//			var roleAdmin=roleService.save(Role.builder().name(ADMIN).build());
//			var roleManager=roleService.save(Role.builder().name(MANAGER).build());
//			var roleUser=roleService.save(Role.builder().name(USER).build());
//
//			//userRole
//			roleService.addRoleWithUser(roleAdmin.getId(),saveAdmin.getId());
//			roleService.addRoleWithUser(roleUser.getId(),saveManager.getId());
//			roleService.addRoleWithUser(roleManager.getId(),saveManager.getId());
//			//permission
//			var auth=permissionService.save(Permission.builder().name("auth").path("auth").build());
//			var user=permissionService.save(Permission.builder().name("user").path("user").build());
//			var permission=permissionService.save(Permission.builder().name("permissions").path("permissions").build());
//			var management=permissionService.save(Permission.builder().name("management").path("management").build());
//			var book=permissionService.save(Permission.builder().name("book").path("book").build());
//
//
//			//userPermission
//			userService.saveUserPermission(UserPermission.builder().userId(saveAdmin.getId()).permissionId(auth.getId()).build());
//			userService.saveUserPermission(UserPermission.builder().userId(saveAdmin.getId()).permissionId(user.getId()).build());
//			userService.saveUserPermission(UserPermission.builder().userId(saveAdmin.getId()).permissionId(permission.getId()).build());
//			userService.saveUserPermission(UserPermission.builder().userId(saveAdmin.getId()).permissionId(book.getId()).build());
//
//			userService.saveUserPermission(UserPermission.builder().userId(saveManager.getId()).permissionId(auth.getId()).build());
//			userService.saveUserPermission(UserPermission.builder().userId(saveManager.getId()).permissionId(management.getId()).build());
//			userService.saveUserPermission(UserPermission.builder().userId(saveManager.getId()).permissionId(book.getId()).build());
//
//		};
//	}
}
