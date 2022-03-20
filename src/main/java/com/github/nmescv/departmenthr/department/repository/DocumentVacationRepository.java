package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.DocumentVacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentVacationRepository extends JpaRepository<DocumentVacation, Long> {
}
