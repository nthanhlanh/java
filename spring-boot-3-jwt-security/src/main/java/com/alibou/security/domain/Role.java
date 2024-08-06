package com.alibou.security.domain;

import com.alibou.security.dto.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_role")
@SqlResultSetMapping(
        name = "RoleMapping",
        entities = @EntityResult(
                entityClass = Role.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "name", column = "name")
                }
        )
)
public class Role  {

  @Id
  @GeneratedValue
  private Integer id;

  @Enumerated(EnumType.STRING)
  private RoleType name;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users = new HashSet<>();

}
