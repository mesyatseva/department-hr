package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.DocumentDismissal;
import com.github.nmescv.departmenthr.department.entity.DocumentHiring;
import com.github.nmescv.departmenthr.department.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentHiringRepository extends JpaRepository<DocumentHiring, Long> {

    List<DocumentHiring> findAllByBoss(Employee employee);
}
