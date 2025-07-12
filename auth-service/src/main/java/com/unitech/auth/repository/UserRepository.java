package com.unitech.auth.repository;

import com.unitech.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email OR u.username = :username")
    boolean existsByEmailOrUsername(@Param("email") String email,
                                    @Param("username") String username);
}
