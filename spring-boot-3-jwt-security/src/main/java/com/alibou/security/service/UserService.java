package com.alibou.security.service;

import com.alibou.security.domain.UserPermission;
import com.alibou.security.dto.UserDto;
import com.alibou.security.mapper.UserMapper;
import com.alibou.security.repository.UserPermissionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alibou.security.domain.User;
import com.alibou.security.dto.ChangePasswordRequest;
import com.alibou.security.repository.UserRepository;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final UserPermissionRepository userPermissionRepository;


    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);

    }


    public UserDto getUserById(@NonNull final UUID id) {
        Optional<User> user=repository.findById(id);
        if(user.isEmpty()){
            throw new NullPointerException();
        }
        return userMapper.toDto(user.get());
    }

    public User save(@NonNull final User user){
        return repository.save(user);
    }

    public UserPermission saveUserPermission(@NonNull final UserPermission userPermission){
        return userPermissionRepository.save(userPermission);
    }
}
