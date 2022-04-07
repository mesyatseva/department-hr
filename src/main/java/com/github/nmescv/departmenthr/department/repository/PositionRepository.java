package com.github.nmescv.departmenthr.department.repository;

import com.github.nmescv.departmenthr.department.entity.Department;
import com.github.nmescv.departmenthr.department.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    Position findByName(String position);

    @Query(
            value = "select p.* from Positions p left join department_position dp on p.id = dp.position_id where dp.department_id = :department_id",
            nativeQuery = true)
    List<Position> findAllByDepartment(@Param("department_id") Long departmentId);

    @Query(
            value = "select * from Positions p where p.id = :id",
            nativeQuery = true)
    Position findByIdy(@Param("id") Long id);
}
