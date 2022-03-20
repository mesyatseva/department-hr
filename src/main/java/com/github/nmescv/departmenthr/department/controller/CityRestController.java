package com.github.nmescv.departmenthr.department.controller;

import com.github.nmescv.departmenthr.department.dao.DaoCity;
import com.github.nmescv.departmenthr.department.entity.City;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityRestController {

    private final DaoCity daoCity; // Иденф доступа, тип, имя

    public CityRestController(DaoCity daoCity) {
        this.daoCity = daoCity;
    }

    @GetMapping
    public ResponseEntity<List<City>> findAll() {
        return ResponseEntity.ok(daoCity.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<City> findByName(@PathVariable("name") String name) {
        City city = daoCity.findByName(name);
        return ResponseEntity.ok(city);
    }
}
