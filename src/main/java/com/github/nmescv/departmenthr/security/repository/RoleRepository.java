package com.github.nmescv.departmenthr.security.repository;

import com.github.nmescv.departmenthr.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
