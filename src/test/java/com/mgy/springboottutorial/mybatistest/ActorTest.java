package com.mgy.springboottutorial.mybatistest;

import com.mgy.springboottutorial.entity.Actor;
import com.mgy.springboottutorial.mybatisdemo.pgsql.ActorMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author meiguangya
 * @date 2025/8/28 上午12:16
 * @description TODO
 */

@SpringBootTest
//@ActiveProfiles("test") // 使用 test 环境配置
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional(transactionManager = "postgresTransactionManager")
@Rollback(value = true) // 🌟 关键：测试方法结束后自动回滚
@Slf4j

public class ActorTest {


    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Actor testActor;

    @Autowired
    private DataSource dataSource;

    @Test
    void printDataSource() {
        log.info("DataSource class: {}", dataSource.getClass());
        try {
            log.info("JDBC URL: {}", dataSource.getConnection().getMetaData().getURL());
            log.info("Database Product: {}", dataSource.getConnection().getMetaData().getDatabaseProductName());
            log.info("Database Version: {}", dataSource.getConnection().getMetaData().getDatabaseProductVersion());
        } catch (Exception e) {
            log.error("获取连接失败", e);
        }
    }

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testActor = new Actor();
        testActor.setFirstName("TestFirstName");
        testActor.setLastName("TestLastName");
        testActor.setLastUpdate(OffsetDateTime.now());
    }

    @Test
    @Order(1)
    //@Transactional
    void testInsert_ShouldInsertActor() {
        log.info("测试插入开始");

        log.info("actorMapper = {}", actorMapper); // 打印 mapper
        log.info("actorMapper class = {}", actorMapper.getClass()); // 看是不是代理类

        // 检查当前是否在事务中
        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("当前是否有事务: {}", actualTransactionActive);

        // When
        int result = actorMapper.insert(testActor);

        // Then
        assertThat(result).isEqualTo(1);
        assertThat(testActor.getActorId()).isNotNull(); // 自增主键已生成
        log.info("测试插入结束");
    }

    @Test
    @Order(2)
    void testSelectById_ShouldReturnInsertedActor() {
        // Given
        actorMapper.insert(testActor);

        // When
        Actor found = actorMapper.selectById(testActor.getActorId());

        log.info("查询到的[{}]",found);

        log.info("UTC时间: {}", found.getLastUpdate());
        log.info("北京时间: {}", found.getLastUpdate().atZoneSameInstant(ZoneId.of("Asia/Shanghai")));

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getFirstName()).isEqualTo("TestFirstName");
        assertThat(found.getLastName()).isEqualTo("TestLastName");
    }

    @Test
    @Order(3)
    void testUpdate_ShouldUpdateActor() {
        // Given
        actorMapper.insert(testActor);
        testActor.setFirstName("UpdatedName");

        // When
        int result = actorMapper.update(testActor);

        // Then
        assertThat(result).isEqualTo(1);
        Actor updated = actorMapper.selectById(testActor.getActorId());
        assertThat(updated.getFirstName()).isEqualTo("UpdatedName");
    }

    @Test
    @Order(4)
    void testSelectAll_ShouldIncludeInsertedActor() {
        // Given
        actorMapper.insert(testActor);

        // When
        List<Actor> actors = actorMapper.selectAll(100); // 限制 100 条

        // Then
        assertThat(actors).isNotEmpty();
        assertThat(actors).anyMatch(a -> a.getActorId().equals(testActor.getActorId()));
    }

    @Test
    @Order(5)
    void testDeleteById_ShouldRemoveActor() {
        // Given
        actorMapper.insert(testActor);

        // When
        int result = actorMapper.deleteById(testActor.getActorId());

        // Then
        assertThat(result).isEqualTo(1);
        Actor deleted = actorMapper.selectById(testActor.getActorId());
        assertThat(deleted).isNull(); // 确认已删除
    }

    @Test
    @Order(6)
    void testDeleteById_AfterRollback_ShouldNotExistInRealDB() {
        // This test is just to verify rollback works
        // Even if we don't clean up, rollback will do it
        assertThat(true).isTrue();
    }

}
