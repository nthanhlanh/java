package com.alibou.security.dto;

import com.alibou.security.domain.Role;

import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String lastname;
  @NonNull
  private String email;
  @NonNull
  private String password;
  private Set<Role> roles ;
}
