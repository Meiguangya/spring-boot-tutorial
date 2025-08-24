package com.mgy.springboottutorial.jdbctest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Employee employee){

        jdbcTemplate.update("insert into employees(emp_no,birth_date,first_name,last_name,gender,hire_date)" +
                " values (?,?,?,?,?,?)",employee.getEmpNo(),employee.getBirthDate(),employee.getFirstName(),employee.getLastName(),employee.getGender(),employee.getHireDate());
    }

    public List<Employee> findAll(int rows){
        return jdbcTemplate.query("select * from employees limit " + rows, BeanPropertyRowMapper.newInstance(Employee.class));
    }

}
