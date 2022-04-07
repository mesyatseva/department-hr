package com.github.nmescv.departmenthr.department.dto;

import com.github.nmescv.departmenthr.department.entity.Department;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVacationDto {

    private Long id;
    private Date createdAt;
    private String orderNumber;
    private Long employeeId;
    private Long bossId;
    private Long hr;
    private String documentStatus;
    private String department;
    private String position;

    @NotNull(message = "Поле 'Дата начала отпуска' должно быть заполнено")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startAt;
    @NotNull(message = "Поле 'Дата окончания отпуска' должно быть заполнено")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endAt;
    private String vacationType;
    private Boolean isApproved;

    private String employeeFullName;
    private String bossFullName;
    private String hrFullName;
}
