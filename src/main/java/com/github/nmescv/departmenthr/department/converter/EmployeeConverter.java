package com.github.nmescv.departmenthr.department.converter;

import com.github.nmescv.departmenthr.department.dto.EmployeeDto;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.*;
import lombok.Synchronized;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter implements Converter<Employee, EmployeeDto> {

    private final CountryRepository countryRepository;
    private final PositionRepository positionRepository;
    private final EducationRepository educationRepository;
    private final UniversityRepository universityRepository;
    private final DepartmentRepository departmentRepository;
    private final SpecialityRepository specialityRepository;

    public EmployeeConverter(CountryRepository countryRepository,
                             PositionRepository positionRepository,
                             EducationRepository educationRepository,
                             UniversityRepository universityRepository,
                             DepartmentRepository departmentRepository,
                             SpecialityRepository specialityRepository) {
        this.countryRepository = countryRepository;
        this.positionRepository = positionRepository;
        this.educationRepository = educationRepository;
        this.universityRepository = universityRepository;
        this.departmentRepository = departmentRepository;
        this.specialityRepository = specialityRepository;
    }

    @Override
    @Synchronized
    public Employee toEntity(EmployeeDto dto) {

        if (dto == null) {
            return null;
        }

        Employee entity = new Employee();
        entity.setId(dto.getId());
        entity.setTabelNumber(dto.getTabelNumber());
        entity.setSurname(dto.getSurname());
        entity.setName(dto.getName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setBirthday(dto.getBirthday());
        entity.setCitizenship(countryRepository.findByName(dto.getCitizenship()));
        entity.setPassSeries(dto.getPassSeries());
        entity.setPassNumber(dto.getPassNumber());
        entity.setInn(dto.getInn());
        entity.setSnils(dto.getSnils());
        entity.setPosition(positionRepository.findByName(dto.getPosition()));
        entity.setEducation(educationRepository.findByDegree(dto.getEducation()));
        entity.setUniversity(universityRepository.findByName(dto.getUniversity()));
        entity.setTelephone(dto.getTelephone());
        entity.setDepartment(departmentRepository.findByName(dto.getDepartment()));
        entity.setSpeciality(specialityRepository.findByName(dto.getSpeciality()));
        return entity;
    }

    @Override
    @Synchronized
    public EmployeeDto toDto(Employee employee) {

        if (employee == null) {
            return null;
        }

        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setTabelNumber(employee.getTabelNumber());
        dto.setSurname(employee.getSurname());
        dto.setName(employee.getName());
        dto.setMiddleName(employee.getMiddleName());
        dto.setBirthday(employee.getBirthday());
        dto.setCitizenship(employee.getCitizenship().getName());
        dto.setPassSeries(employee.getPassSeries());
        dto.setPassNumber(employee.getPassNumber());
        dto.setInn(employee.getInn());
        dto.setSnils(employee.getSnils());
        dto.setPosition(employee.getPosition().getName());
        dto.setEducation(employee.getEducation().getDegree());
        dto.setUniversity(employee.getUniversity().getName());
        dto.setSpeciality(employee.getSpeciality().getName());
        dto.setTelephone(employee.getTelephone());
        dto.setDepartment(employee.getDepartment().getName());
        return dto;
    }
}
