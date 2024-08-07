package com.alibou.security.config;

import com.alibou.security.repository.PermissionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomFilter extends OncePerRequestFilter {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Collection<? extends GrantedAuthority> authoritiesPermissions = authorities.stream()
                    .filter(authority -> !authority.getAuthority().startsWith("ROLE_"))
                    .toList();
            String path = request.getRequestURI();
//            System.out.println("Request Path: " + path);
//            System.out.println("All authoritiesPermissions: " + authoritiesPermissions);
            boolean hasAccess = authoritiesPermissions.stream()
                    .anyMatch(authority -> path.contains(authority.getAuthority())); // Kiểm tra nếu quyền trùng với đường dẫn

            if (!hasAccess) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                response.getWriter().write("access denied");
                return;
            }
        }
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
