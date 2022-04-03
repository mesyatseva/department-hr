package com.github.nmescv.departmenthr.department.dto;

import com.github.nmescv.departmenthr.department.entity.Department;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDismissalDto {

    private Long id;
    private Date createdAt;
    private String orderNumber;
    private Long employeeId;
    private Long bossId;
    private Long hr;
    private String documentStatus;
    private String department;
    private String position;

    @NotNull(message = "Поле 'Дата увольнения' должно быть заполнено")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dismissalDate;
    private String reason;
    private Boolean isApproved;
}
