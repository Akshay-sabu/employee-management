package com.employee.management.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeDTO {
    private Long employeeID;
    private String employeeName;
    private String designation;
    private String location;
    private String bankName;
    private String accountNo;
    private String dateOfJoin;
    private String password;
}
