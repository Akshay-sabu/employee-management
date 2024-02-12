package com.employee.management.schedulers;

import com.employee.management.models.Employee;
import com.employee.management.models.Payroll;
import com.employee.management.repository.AttendanceRepository;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Component
public class PayrollUpdater {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PayrollRepository payrollRepository;
    @Autowired
    AttendanceRepository attendanceRepository;

    @Scheduled(cron = "0 0 19 L * ?",zone = "Asia/Kolkata")
    public void updatePayroll(){
        List<Employee> employees=employeeRepository.findAll();
        employees.stream()
                .filter(Objects::nonNull)
                .map(this::generatePayRoll)
                .forEach(this::savePayRoll);
    }

    Payroll generatePayRoll(Employee employee){
        Payroll payroll=new Payroll();
        payroll.setBasic(16000D);
        payroll.setEmployee(employee);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMMM yyyy");
        String payPeriod=simpleDateFormat.format(Calendar.getInstance().getTime());
        payroll.setPayPeriod(payPeriod);
        Date payDate=new Date();
        payroll.setPayDate(payDate);
        payroll.setHouseRentAllowance(payroll.getBasic()*0.1);
        payroll.setMedicalAllowance(payroll.getBasic()*0.05);
        payroll.setOtherAllowance(payroll.getBasic()*0.1);
        Double grossSalary= payroll.getBasic()+payroll.getHouseRentAllowance()+payroll.getMedicalAllowance()+payroll.getOtherAllowance();
        payroll.setGrossEarnings(grossSalary);
        payroll.setProvidentFund(payroll.getBasic()*0.1);
        Double leaveDeduction=getLeaveDeduction(employee.getEmployeeID(),payroll.getBasic());
        payroll.setTotalDeductions(payroll.getProvidentFund()+leaveDeduction);
        payroll.setTotalNetPayable(grossSalary-payroll.getTotalDeductions());
        return payroll;
    }

    private Double getLeaveDeduction(Long employeeID,Double basicSalary) {
        LocalDate date = LocalDate.now();
        java.sql.Date currentDate = java.sql.Date.valueOf(date);
        LocalDate firstDateOfMonth = date.withDayOfMonth(1);
        java.sql.Date firstDay = java.sql.Date.valueOf(firstDateOfMonth);
        Integer totalLeaves= attendanceRepository.getNoOfAbsence(employeeID, firstDay, currentDate);
        YearMonth currentYearMonth = YearMonth.now();
        Integer numberOfDaysInMonth = currentYearMonth.lengthOfMonth();
        Double payPerDay=basicSalary/numberOfDaysInMonth;
        return  Math.round(payPerDay * totalLeaves * 100.0) / 100.0;
    }

    void savePayRoll(Payroll payroll){
        payrollRepository.save(payroll);
    }
}
