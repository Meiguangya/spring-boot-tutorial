package com.mgy.springboottutorial.mybatistest;

import com.mgy.springboottutorial.entity.Salary;
import com.mgy.springboottutorial.mybatisdemo.SalaryMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class SalaryTest {

    @Autowired
    SalaryMapper salaryMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Transactional
    @Test
    public void saveTest(){

        Salary salary = new Salary();
        salary.setEmpNo(10001);
        salary.setSalary(5001);
        salary.setFromDate(LocalDate.of(2020,2,1));
        salary.setToDate(LocalDate.of(2025,2,1));
        log.info("over");
    }

    @Test
    public void listTest(){

        List<Salary> salaries = salaryMapper.selectAll(5);
        for (Salary salary : salaries) {
            System.out.println(salary);
        }

    }

    @Test
    public void testSqlSession(){

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<Salary> list = sqlSession.selectList("com.mgy.springboottutorial.mybatisdemo.SalaryMapper.selectAll",3);
            list.forEach(System.out::println);
        }

    }


}
