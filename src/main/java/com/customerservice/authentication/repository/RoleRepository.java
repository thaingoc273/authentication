package com.customerservice.authentication.repository;

import com.customerservice.authentication.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
  Optional<Role> findByRoleCode(String roleCode);
}
