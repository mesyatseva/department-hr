package com.github.nmescv.departmenthr.department.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelativePersonDto {
    private Long id;
    private String surname;
    private String name;
    private String middleName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date birthday;
    private String relationship;
    private Long employeeId;
}
