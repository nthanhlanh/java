package com.alibou.security.domain;

import com.alibou.security.dto.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Audited
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
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
public class Role extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private RoleType name;

}
