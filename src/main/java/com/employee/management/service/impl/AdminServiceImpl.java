package com.employee.management.service.impl;

import com.employee.management.DTO.AdminDashBoardData;
import com.employee.management.DTO.EmployeeDTO;
import com.employee.management.DTO.PayrollDTO;
import com.employee.management.converters.DateTimeConverter;
import com.employee.management.converters.Mapper;
import com.employee.management.exception.CompanyException;
import com.employee.management.exception.ResCodes;
import com.employee.management.models.Employee;
import com.employee.management.models.Payroll;
import com.employee.management.models.Role;
import com.employee.management.models.Status;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.PayrollRepository;
import com.employee.management.repository.RoleRepository;
import com.employee.management.repository.StatusRepository;
import com.employee.management.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PayrollRepository payrollRepository;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    Mapper mapper;
    @Autowired
    PasswordEncoder passwordEncoder;


    private String getTodayDateFormatted(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return today.format(formatter);
    }
    private int getEmployeeCount(){
        List<Employee>employees=employeeRepository.findAll();
        return employees.size();
    }
    @Override
    public EmployeeDTO addNewEmployee(EmployeeDTO employeeDTO){
        Employee employee=mapper.convertToEmployeeEntity(employeeDTO);
        Role role=roleRepository.findById(2L).get();
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.getRoles().add(role);
        employee.setStatus(statusRepository.findById(1L).get());
        Employee savedEmployee = employeeRepository.save(employee);
        return mapper.convertToEmployeeDTO(savedEmployee);
    }

    @Override
    public AdminDashBoardData loadData(){
        AdminDashBoardData adminDashBoardData=new AdminDashBoardData();
        YearMonth currentYearMonth = YearMonth.now();
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        String previousMonthFormatted = previousYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                + " " + previousYearMonth.getYear();
        List<Payroll>payrolls=payrollRepository.getPayDetails(previousMonthFormatted)
                .orElseThrow(()->new CompanyException(ResCodes.SALARY_DETAILS_NOT_FOUND));
        Double averageSalary=payrolls.stream()
                .mapToDouble(Payroll::getGrossEarnings)
                .average()
                .orElse(0.0);

        adminDashBoardData.setAverageSalary(averageSalary);
        adminDashBoardData.setTodayDate(getTodayDateFormatted());
        adminDashBoardData.setNoOfEmployees(getEmployeeCount());
        return adminDashBoardData;
    }
    @Override
    public List<EmployeeDTO> fetchAllActiveEmployees(){
        List<Employee>employees=employeeRepository.findAll();
        return  employees.stream()
                .filter(Objects::nonNull)
                .filter(employee -> employee.getStatus().getName().equals("active"))
                .map(mapper::convertToEmployeeDTO)
                .toList();
    }

    @Override
    public EmployeeDTO editEmployee(String empId,EmployeeDTO employeeDTO){
        Employee employee =employeeRepository.findById(empId)
                .orElseThrow(()-> new CompanyException(ResCodes.EMPLOYEE_NOT_FOUND));
        employee.setDesignation(employeeDTO.getDesignation());
        employee.setLocation(employeeDTO.getLocation());
        employee.setBankName(employeeDTO.getBankName());
        employee.setAccountNo(employeeDTO.getAccountNo());
        employee.setEmployeeName(employeeDTO.getEmployeeName());
        Employee savedEmployee = employeeRepository.save(employee);
        return mapper.convertToEmployeeDTO(savedEmployee);
    }

    @Override
    public String changeEmployeeStatus(String empId, String empStatus){

        Employee employee=employeeRepository.findById(empId)
                .orElseThrow(()->new CompanyException(ResCodes.EMPLOYEE_NOT_FOUND));
        Status status=statusRepository.findByName(empStatus.toLowerCase())
                .orElseThrow(()->new CompanyException(ResCodes.INVALID_STATUS));
        employee.setStatus(status);
        employeeRepository.save(employee);
        return "Employee status changed successfully";
    }
    public PayrollDTO addPayroll(PayrollDTO payrollDTO){

    }

}
