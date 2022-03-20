package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    Speciality findByName(String name);
}
