package com.employee.management.converters;

import com.employee.management.DTO.EmployeeDTO;
import com.employee.management.DTO.PayrollDTO;
import com.employee.management.exception.CompanyException;
import com.employee.management.exception.ResCodes;
import com.employee.management.models.Employee;
import com.employee.management.models.Payroll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Mapper {
    @Autowired
    DateTimeConverter dateConverter;
    public EmployeeDTO convertToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO=new EmployeeDTO();
        if(employee!=null){
            employeeDTO.setEmployeeID(employee.getEmployeeID());
            employeeDTO.setEmployeeName(employee.getEmployeeName());
            employeeDTO.setDesignation(employee.getDesignation());
            employeeDTO.setLocation(employee.getLocation());
            employeeDTO.setBankName(employee.getBankName());
            employeeDTO.setAccountNo(employee.getAccountNo());
            employeeDTO.setDateOfJoin(dateConverter.localDateTimeToStringConverter(employee.getDateOfJoin()));
        }
        return employeeDTO;
    }
    public PayrollDTO convertToPayRollDTO(Payroll payroll) {
        PayrollDTO dto = new PayrollDTO();
        dto.setId(payroll.getId());
        dto.setPayPeriod(payroll.getPayPeriod());
        dto.setPayDate(dateConverter.localDateTimeToStringConverter(payroll.getPayDate()));
        dto.setEmployeeId(payroll.getEmployee().getEmployeeID());
        dto.setBasic(payroll.getBasic());
        dto.setHouseRentAllowance(payroll.getHouseRentAllowance());
        dto.setMedicalAllowance(payroll.getMedicalAllowance());
        dto.setOtherAllowance(payroll.getOtherAllowance());
        dto.setGrossEarnings(payroll.getGrossEarnings());
        dto.setProvidentFund(payroll.getProvidentFund());
        dto.setTotalDeductions(payroll.getTotalDeductions());
        dto.setTotalNetPayable(payroll.getTotalNetPayable());
        return dto;
    }
    public Employee convertToEmployeeEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        if(!validateEmployeeDto(employeeDTO)){
            throw new CompanyException(ResCodes.INVALID_EMPLOYEE_DETAILS);
        }
        employee.setEmployeeName(employeeDTO.getEmployeeName());
        employee.setDesignation(employeeDTO.getDesignation());
        employee.setLocation(employeeDTO.getLocation());
        employee.setBankName(employeeDTO.getBankName());
        employee.setAccountNo(employeeDTO.getAccountNo());
        employee.setPassword(employeeDTO.getPassword());
        employee.setDateOfJoin(dateConverter.stringToLocalDateTimeConverter(employeeDTO.getDateOfJoin()));
        return employee;
    }
    private boolean validateEmployeeDto(EmployeeDTO employeeDTO) {
        return Arrays.asList(employeeDTO.getEmployeeName(), employeeDTO.getDesignation(),
                        employeeDTO.getLocation(), employeeDTO.getBankName(),
                        employeeDTO.getAccountNo())
                .stream()
                .allMatch(field -> field != null && !field.isEmpty());
    }
}
