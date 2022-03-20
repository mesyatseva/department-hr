package com.github.nmescv.departmenthr.security.repository;

import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmployee(Employee employee);
}
