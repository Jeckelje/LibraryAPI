package com.modsen.booktrackerservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@Schema(description = "User Entity, represents the user details for authentication and authorization.")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user.", example = "1")
    private Long id;

    @Column(nullable = false, unique = true, name = "username", length = 64)
    @Schema(description = "User's username, must be unique.", example = "john_doe")
    private String username;

    @Column(nullable = false, name = "password", length = 256)
    @Schema(description = "User's password, stored in hashed format.", example = "password123")
    private String password;

    @Transient
    @Schema(description = "Field for password confirmation during registration. Not stored in the database.", example = "password123")
    private String passwordConfirm;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Schema(description = "Role assigned to the user for authorization.", example = "[\"ROLE_ADMIN\", \"ROLE_USER\"]")
    private Set<Role> roles;

    @Column(nullable = false, name = "enabled")
    @Schema(description = "Indicates whether the user account is enabled or not.", example = "true")
    private boolean enabled = true;

}
