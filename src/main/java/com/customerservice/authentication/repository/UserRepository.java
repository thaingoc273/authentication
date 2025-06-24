package com.customerservice.authentication.repository;

import com.customerservice.authentication.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByUsername(String username);

  @Query("SELECT DISTINCT u from User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
  Optional<User> findByUsernameWithRoles(@Param("username") String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
