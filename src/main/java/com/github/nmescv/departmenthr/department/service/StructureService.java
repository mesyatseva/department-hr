package com.github.nmescv.departmenthr.department.service;

import com.github.nmescv.departmenthr.department.dto.StructureDto;
import com.github.nmescv.departmenthr.department.entity.Department;
import com.github.nmescv.departmenthr.department.entity.Employee;
import com.github.nmescv.departmenthr.department.repository.DepartmentRepository;
import com.github.nmescv.departmenthr.department.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class StructureService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public StructureService(EmployeeRepository employeeRepository,
                            DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    /**
     * Отобразить составляющие сотрудника в подразделении
     * - Название подразделения
     * - Должность
     * - Начальник
     *
     * @return информация
     */
    public StructureDto makeStructureOfDepartment(String username) {
        Employee employee = employeeRepository.findByTabelNumber(username);
        Department department = departmentRepository.findByName(employee.getDepartment().getName());
        String position = employee.getPosition().getName();
        String boss = buildFullName(department.getBoss());
        return new StructureDto(department.getName(), position, boss, department.getBoss().getId());
    }

    private String buildFullName(Employee employee) {
        if (employee.getMiddleName() == null) {
            return employee.getSurname() + " " + employee.getName();
        } else {
            return employee.getSurname() + " " + employee.getName() + " " + employee.getMiddleName();
        }
    }
}
