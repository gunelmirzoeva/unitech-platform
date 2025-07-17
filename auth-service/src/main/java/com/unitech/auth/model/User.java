package com.unitech.auth.model;

import com.unitech.auth.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users",indexes = {
@Index(columnList = "email", unique = true),
@Index(columnList = "username", unique = true)
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

}
