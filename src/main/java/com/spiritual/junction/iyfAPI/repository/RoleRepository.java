package com.spiritual.junction.iyfAPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiritual.junction.iyfAPI.domain.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(String role);

    List<Role> findByRoleIn(List<String> roleList);
}
