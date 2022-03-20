package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.converter.RelativePersonConverter;
import com.github.nmescv.departmenthr.department.dto.RelativePersonDto;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.entity.RelativePerson;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import com.github.nmescv.departmenthr.department.repository.RelativePersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelativePersonService {

    private final EmployeeRepository employeeRepository;
    private final RelativePersonRepository relativePersonRepository;
    private final RelativePersonConverter relativePersonConverter;

    public RelativePersonService(EmployeeRepository employeeRepository,
                                 RelativePersonRepository relativePersonRepository,
                                 RelativePersonConverter relativePersonConverter) {
        this.employeeRepository = employeeRepository;
        this.relativePersonRepository = relativePersonRepository;
        this.relativePersonConverter = relativePersonConverter;
    }

    public List<RelativePersonDto> showRelativePersonListByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            return null;
        }
        List<RelativePerson> list = relativePersonRepository.findAllByEmployee(employee);
        return list
                .stream()
                .map(relativePersonConverter::toDto)
                .collect(Collectors.toList());
    }

    public RelativePersonDto getRelativePersonById(Long personId) {
        RelativePerson relativePerson = relativePersonRepository.findById(personId).orElse(null);
        if (relativePerson == null) {
            return null;
        }
        return relativePersonConverter.toDto(relativePerson);
    }

    public RelativePersonDto createRelativePerson(RelativePersonDto dto, Long employeeId) {
        dto.setEmployeeId(employeeId);
        RelativePerson relativePerson = relativePersonConverter.toEntity(dto);
        RelativePerson savedPerson = relativePersonRepository.save(relativePerson);
        return relativePersonConverter.toDto(savedPerson);
    }
}
