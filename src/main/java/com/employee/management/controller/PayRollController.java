package com.employee.management.controller;

import com.employee.management.DTO.PaySlip;
import com.employee.management.DTO.PayrollRequest;
import com.employee.management.exception.CompanyException;
import com.employee.management.exception.ResCodes;
import com.employee.management.models.Payroll;
import com.employee.management.service.EmployeeService;
import com.employee.management.service.PayRollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salary")
public class PayRollController {
    @Autowired
    PayRollService payRollService;
    @Autowired
    EmployeeService employeeService;
    @GetMapping("/get")
    public ResponseEntity<PaySlip> getPaySlip(@RequestBody PayrollRequest payrollRequest){
        Long empId=payrollRequest.getEmployeeId();
        String payPeriod=payrollRequest.getPayPeriod();
        String password=payrollRequest.getPassword();
        if(employeeService.verifyUser(empId,password))
             return new ResponseEntity<>(payRollService.getPaySlip(empId,payPeriod), HttpStatus.OK);
        else throw new CompanyException(ResCodes.INVALID_ID_AND_PASSWORD);
    }
}
