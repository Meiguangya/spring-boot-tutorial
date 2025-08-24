package com.mgy.springboottutorial.jdbctest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Transactional(rollbackFor = Exception.class)
    public void test(int empNo) {

        Employee employee = new Employee();
        employee.setEmpNo(empNo);
        employee.setGender("M");
        employee.setBirthDate(LocalDate.of(1989, 1, 1));
        employee.setHireDate(LocalDate.of(1999, 12, 1));
        employee.setFirstName("Jack");
        employee.setLastName("Sam");

        employeeDao.save(employee);

        int i = 1 / empNo;

        log.info("插入数据完成");

    }

    public void listEmployee(int row) {
        List<Employee> list = employeeDao.findAll(row);
        list.forEach(System.out::println);
    }

}
