package com.employee.management.service;

import com.employee.management.DTO.EmployeeDTO;

public interface EmployeeService {
    EmployeeDTO getEmployee(Long id);
    boolean verifyUser(Long id,String password);

    EmployeeDTO addNewEmployee(EmployeeDTO employeeDTO);
}
