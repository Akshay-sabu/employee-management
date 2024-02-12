package com.employee.management.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "Employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID")
    private Long employeeID;

    @Column(name = "EmployeeName")
    private String employeeName;

    @Column(name = "Designation")
    private String designation;

    @Column(name = "Location")
    private String location;

    @Column(name = "BankName")
    private String bankName;

    @Column(name = "AccountNo")
    private String accountNo;

    @Column(name = "DateOfJoin")
    private Date dateOfJoin;

    @Column(name = "Password")
    private String password;
}

