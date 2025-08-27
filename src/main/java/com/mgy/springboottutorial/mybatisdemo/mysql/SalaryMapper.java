package com.mgy.springboottutorial.mybatisdemo.mysql;

import com.mgy.springboottutorial.entity.Salary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SalaryMapper {

    void save(Salary salary);

    void update(Salary salary);

    List<Salary> selectAll(int rows);

}
