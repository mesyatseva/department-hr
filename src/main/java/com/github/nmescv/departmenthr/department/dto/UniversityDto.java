package com.github.nmescv.departmenthr.department.dto;

import com.github.nmescv.departmenthr.department.entity.City;
import com.github.nmescv.departmenthr.department.entity.Country;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityDto {

    private Long id;
    private String name;
    private String country;
    private String city;
}
