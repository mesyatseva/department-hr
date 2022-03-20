package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.DocumentDismissal;
import com.github.nmescv.departmenthr.department.entity.DocumentHiring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentHiringRepository extends JpaRepository<DocumentHiring, Long> {
}
