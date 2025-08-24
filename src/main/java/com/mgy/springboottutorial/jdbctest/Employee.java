package com.mgy.springboottutorial.jdbctest;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Employee {

    private Integer empNo;

    private LocalDate hireDate;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String gender;

    private Integer version;
}
