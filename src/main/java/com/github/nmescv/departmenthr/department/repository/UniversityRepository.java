package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    University findByName(String university);
}
