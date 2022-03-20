package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.DocumentReassignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentReassignmentRepository extends JpaRepository<DocumentReassignment, Long> {
}
