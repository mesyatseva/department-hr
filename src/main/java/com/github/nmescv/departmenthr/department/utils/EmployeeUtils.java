package com.github.nmescv.departmenthr.department.utils;

import com.github.nmescv.departmenthr.department.entity.Employee;

public class EmployeeUtils {

    public static String fullName(Employee employee) {
        if (employee.getMiddleName() != null) {
            return employee.getSurname() + " " + employee.getName().charAt(0) + "." + employee.getMiddleName().charAt(0) + ".";
        } else {
            return employee.getSurname() + " " + employee.getName().charAt(0) + ".";
        }
    }
}
