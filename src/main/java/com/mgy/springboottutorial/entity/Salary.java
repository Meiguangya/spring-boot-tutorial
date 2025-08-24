package com.mgy.springboottutorial.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Salary {

    private Integer empNo;

    private Integer salary;

    private LocalDate fromDate;

    private LocalDate toDate;

}
