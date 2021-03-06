package com.example.logindemo.repository;

import com.example.logindemo.models.ERole;
import com.example.logindemo.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole name);

    Optional<Role> findById(Integer id);
}