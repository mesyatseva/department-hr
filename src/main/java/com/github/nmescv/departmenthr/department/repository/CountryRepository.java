package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByName(String name);
}
