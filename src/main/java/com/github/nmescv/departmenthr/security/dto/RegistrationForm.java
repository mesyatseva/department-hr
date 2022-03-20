package com.github.nmescv.departmenthr.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class RegistrationForm {

    private Long id;
    private Long employeeId;
    private String password;
    private String confirmedPassword;
    private String accountStatus;
    private Date createdDate;
    private String role;
}
