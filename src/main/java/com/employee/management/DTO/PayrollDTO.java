package com.employee.management.DTO;

import lombok.Data;
import java.util.Date;

@Data
public class PayrollDTO {
    private Long id;
    private String payPeriod;
    private String payDate;
    private Long employeeId;
    private Double basic;
    private Double houseRentAllowance;
    private Double medicalAllowance;
    private Double otherAllowance;
    private Double grossEarnings;
    private Double providentFund;
    private Double totalDeductions;
    private Double totalNetPayable;
    private Integer totalDaysPaid;
}
