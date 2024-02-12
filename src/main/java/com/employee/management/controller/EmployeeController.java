package com.employee.management.controller;

import com.employee.management.DTO.EmployeeDTO;
import com.employee.management.exception.CompanyException;
import com.employee.management.exception.ResCodes;
import com.employee.management.service.AttendanceService;
import com.employee.management.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    AttendanceService attendanceService;

    @GetMapping("/get/{empId}")
    public ResponseEntity<EmployeeDTO>getEmployee(@PathVariable("empId")Long id, @RequestParam("password")String password){
        if(employeeService.verifyUser(id,password))
             return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
        else throw new CompanyException(ResCodes.INVALID_ID_AND_PASSWORD);
    }
    @PostMapping("/add")
    public ResponseEntity<EmployeeDTO>addEmployee(@RequestBody EmployeeDTO employeeDTO){
        return new ResponseEntity<>(employeeService.addNewEmployee(employeeDTO),HttpStatus.CREATED);
    }
    @GetMapping("/at")
    public int attendance(){
        return attendanceService.getAttendance();
    }

}
