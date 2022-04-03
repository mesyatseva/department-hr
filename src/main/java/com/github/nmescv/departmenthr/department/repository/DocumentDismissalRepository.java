package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.DocumentDismissal;
import com.github.nmescv.departmenthr.department.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DocumentDismissalRepository extends JpaRepository<DocumentDismissal, Long> {
    List<DocumentDismissal> findAllByBoss(Employee employee);

    List<DocumentDismissal> findAllByEmployee(Employee employee);
}
