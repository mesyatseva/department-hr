package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.entity.RelativePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelativePersonRepository extends JpaRepository<RelativePerson, Long> {

    List<RelativePerson> findAllByEmployee(Employee employee);
}
