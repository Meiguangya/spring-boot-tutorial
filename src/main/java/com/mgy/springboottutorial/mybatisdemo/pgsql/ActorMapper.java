package com.mgy.springboottutorial.mybatisdemo.pgsql;

import com.mgy.springboottutorial.entity.Actor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author meiguangya
 * @date 2025/8/28 上午12:12
 * @description TODO
 */
@Mapper
public interface ActorMapper {


    /**
     * 插入新演员
     */
    int insert(Actor actor);

    /**
     * 根据 ID 查询演员
     */
    Actor selectById(Integer actorId);

    /**
     * 查询所有演员（可限制数量）
     */
    List<Actor> selectAll(@Param("limit") Integer limit);

    /**
     * 更新演员信息
     */
    int update(Actor actor);

    /**
     * 删除演员
     */
    int deleteById(Integer actorId);

}
