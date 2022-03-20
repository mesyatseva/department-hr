package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
    City findByName(String name);

}
