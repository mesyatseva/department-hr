package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.DocumentReassignment;
import com.github.nmescv.departmenthr.department.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DocumentReassignmentRepository extends JpaRepository<DocumentReassignment, Long> {
    List<DocumentReassignment> findAllByEmployee(Employee employee);
    List<DocumentReassignment> findAllByBoss(Employee boss);
}
