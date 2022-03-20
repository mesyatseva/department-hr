package com.github.nmescv.departmenthr.department.dao;

import com.github.nmescv.departmenthr.department.entity.City;
import com.github.nmescv.departmenthr.department.repository.CityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DaoCity {

    private final CityRepository cityRepository;

    public DaoCity(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public City findByName(String name) {
        return cityRepository.findByName(name);
    }
}
