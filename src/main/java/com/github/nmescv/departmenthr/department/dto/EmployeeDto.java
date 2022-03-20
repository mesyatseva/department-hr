package com.github.nmescv.departmenthr.department.dto;

import com.github.nmescv.departmenthr.department.entity.Country;
import com.github.nmescv.departmenthr.department.entity.Department;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long id;

    @NotBlank(message = "Поле 'Tabel Number' должно быть заполнено уникальным значением")
    private String tabelNumber;

    @NotBlank(message = "Поле 'Surname' должно быть заполнено")
    @Size(min = 1, max = 200, message = "Поле 'Surname' имеет мин. длину - 2, макс. - 255")
    private String surname;

    @NotBlank(message = "Поле 'Name' должно быть заполнено")
    @Size(min = 1, max = 200, message = "Поле 'Name' имеет мин. длину - 2, макс. - 255")
    private String name;

    private String middleName;

    @NotNull(message = "Поле 'BirthDay' должно быть заполнено")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date birthday;

    @NotBlank(message = "Поле 'Citizenship' должно быть заполнено")
    private String citizenship;

    @NotBlank(message = "Поле 'Passport Series' должно быть заполнено")
    private String passSeries;
    @NotBlank(message = "Поле 'Passport Number' должно быть заполнено")
    private String passNumber;

    @NotBlank(message = "Поле 'Inn' должно быть заполнено")
    @Size(min = 12, max = 12, message = "Inn has only 12 numbers")
    private String inn;

    @NotBlank(message = "Поле 'Snils' должно быть заполнено")
    @Size(min = 11, max = 11, message = "Snils has only 11 numbers")
    private String snils;

    @NotBlank(message = "Поле 'Position' должно быть заполнено")
    private String position;

    @NotBlank(message = "Поле 'Education' должно быть заполнено")
    private String education;

    @NotBlank(message = "Поле 'University' должно быть заполнено")
    private String university;

    @NotBlank(message = "Поле 'Speciality' должно быть заполнено")
    private String speciality;

    @NotBlank(message = "Поле 'Telephone' должно быть заполнено")
    private String telephone;

    @NotBlank(message = "Поле 'Department' должно быть заполнено")
    private String department;
}
