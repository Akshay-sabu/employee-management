package com.employee.management.service;

import com.employee.management.DTO.AdminDashBoardData;
import com.employee.management.DTO.EmployeeDTO;

import java.util.List;

public interface AdminService {
    EmployeeDTO addNewEmployee(EmployeeDTO employeeDTO);

    AdminDashBoardData loadData();

    List<EmployeeDTO> fetchAllActiveEmployees();

    EmployeeDTO editEmployee(String empId,EmployeeDTO employeeDTO);

    String changeEmployeeStatus(String empId, String empStatus);
}
