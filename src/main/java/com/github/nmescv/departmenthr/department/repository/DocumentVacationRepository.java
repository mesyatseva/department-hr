package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.DocumentVacation;
import com.github.nmescv.departmenthr.department.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DocumentVacationRepository extends JpaRepository<DocumentVacation, Long> {
    List<DocumentVacation> findAllByBoss(Employee employee);
    List<DocumentVacation> findAllByEmployee(Employee employee);
}
