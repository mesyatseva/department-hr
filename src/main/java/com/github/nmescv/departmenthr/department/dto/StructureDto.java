package com.github.nmescv.departmenthr.department.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Описание принадлежности к подразделению:
 *
 * - Подразделение
 * - Должность
 * - Начальник
 */
@Data
@AllArgsConstructor
public class StructureDto {

    private String department;
    private String position;
    private String bossFullName;
    private Long bossId;

//    // ------------------------
//    private String oldDepartment;
//    private String oldPosition;

}
