package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentStatusRepository extends JpaRepository<DocumentStatus, Long> {

    DocumentStatus findByName(String documentStatus);
}
