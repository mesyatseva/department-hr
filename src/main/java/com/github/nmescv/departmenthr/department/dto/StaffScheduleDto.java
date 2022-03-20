package com.github.nmescv.departmenthr.department.dto;

import com.github.nmescv.departmenthr.department.entity.Department;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffScheduleDto {
    private Long id;
    private String position;
    private String department;
    private Integer employeeNumber;
    private Double minimalSalary;
    private Double maximalSalary;
}
