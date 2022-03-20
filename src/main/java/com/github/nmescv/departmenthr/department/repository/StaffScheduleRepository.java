package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.Department;
import com.github.nmescv.departmenthr.department.entity.StaffSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffScheduleRepository extends JpaRepository<StaffSchedule, Long> {

    StaffSchedule findByDepartment(Department department);

    List<StaffSchedule> findAllByDepartment(Department department);
}
