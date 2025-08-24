package com.mgy.springboottutorial.jdbctest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void contextLoads() {
        assertThat(employeeService).isNotNull();
    }

    @Test
    void testInsert(){
        employeeService.test(0);
    }

    @Test
    void testList(){
        employeeService.listEmployee(20);
    }

}
