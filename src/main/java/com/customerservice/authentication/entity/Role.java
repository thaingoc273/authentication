package com.customerservice.authentication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@EqualsAndHashCode(exclude = "users")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(length = 36, updatable = false)
  private String id;

  @NotBlank(message = "Role code is required")
  @Size(min = 5, max = 50, message = "Role code must be between 6 and 50 characters")
  @Column(name = "role_code", length = 50, nullable = false, unique = true)
  private String roleCode;

  @NotBlank(message = "Role type is required")
  @Size(min = 5, max = 50, message = "Role type must be between 6 and 50 characters")
  @Column(name = "role_type", length = 50)
  private String roleType;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users = new HashSet<>();

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
