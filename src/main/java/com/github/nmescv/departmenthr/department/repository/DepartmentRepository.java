package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByName(String departmentName);
}
