package com.employee.management.DTO;

import lombok.Data;

@Data
public class PayrollRequest {
    private Long employeeId;
    private String payPeriod;
    private String password;
}