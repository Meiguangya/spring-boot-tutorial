package com.mgy.springboottutorial.redis;

import com.mgy.springboottutorial.config.RedisConfig;
import com.mgy.springboottutorial.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

/**
 * 纯粹的 Redis 功能单元测试（不涉及业务）
 * 使用 @DataRedisTest 只加载 Redis 相关组件
 */
@DataRedisTest
@Import({RedisConfig.class,RedisUtil.class})
//@TestPropertySource(properties = {
//        "spring.data.redis.host=localhost",
//        "spring.data.redis.port=6379",
//        "spring.data.redis.password=",
//        "spring.data.redis.database=0",
//        "spring.data.redis.timeout=5s",
//        "spring.data.redis.lettuce.pool.enabled=true",
//        "spring.data.redis.lettuce.pool.max-active=8"
//})
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RedisFunctionalityTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisScript<Long> setWithExpireScript;

    @Autowired
    private RedisScript<Long> couponDecr;

    private static final String STRING_KEY = "test:string";
    private static final String INCR_KEY = "test:counter";
    private static final String HASH_KEY = "test:hash";
    private static final String LIST_KEY = "test:list";

    @BeforeEach
    void setUp() {
        // 清理测试数据
        stringRedisTemplate.delete(STRING_KEY);
        stringRedisTemplate.delete(INCR_KEY);
        stringRedisTemplate.delete(HASH_KEY);
        stringRedisTemplate.delete(LIST_KEY);
    }

    @Test
    @Order(1)
    void should_SetAndGet_String() {
        // Given
        String value = "Hello Redis from Spring Boot 3!";

        // When
        stringRedisTemplate.opsForValue().set(STRING_KEY, value, Duration.ofSeconds(10));
        String result = stringRedisTemplate.opsForValue().get(STRING_KEY);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(value);
        assertThat(stringRedisTemplate.hasKey(STRING_KEY)).isTrue();
    }

    @Test
    @Order(2)
    void should_Increment_Number() {
        // Given
        stringRedisTemplate.delete(INCR_KEY);
        stringRedisTemplate.opsForValue().set(INCR_KEY, "100");

        // When
        Long newValue = stringRedisTemplate.opsForValue().increment(INCR_KEY); // +1
        Long newValueBy5 = stringRedisTemplate.opsForValue().increment(INCR_KEY, 5); // +5

        // Then
        assertThat(newValue).isEqualTo(101L);
        assertThat(newValueBy5).isEqualTo(106L);
    }

    @Test
    @Order(3)
    void should_Handle_Expiration() {
        // Given
        stringRedisTemplate.opsForValue().set(STRING_KEY, "expiring data", Duration.ofSeconds(2));

        // Then
        assertThat(stringRedisTemplate.hasKey(STRING_KEY)).isTrue();

        // Wait 3 seconds
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Should expire
        assertThat(stringRedisTemplate.hasKey(STRING_KEY)).isFalse();
    }

    @Test
    @Order(4)
    void should_Handle_Hash_Operations() {
        // Given
        Map<String, String> hashData = new HashMap<>();
        hashData.put("name", "Alice");
        hashData.put("age", "30");
        hashData.put("city", "Beijing");

        // When
        stringRedisTemplate.opsForHash().putAll(HASH_KEY, hashData);

        // Get single field
        String name = (String) stringRedisTemplate.opsForHash().get(HASH_KEY, "name");

        // Get all
        Map<Object, Object> all = stringRedisTemplate.opsForHash().entries(HASH_KEY);

        // Then
        assertThat(name).isEqualTo("Alice");
        assertThat(all).hasSize(3);
        assertThat(all).containsKey("city");
        assertThat(all.get("age")).isEqualTo("30");
    }

    @Test
    @Order(5)
    void should_Handle_List_Operations() {
        // Given
        stringRedisTemplate.delete(LIST_KEY);

        // When
        stringRedisTemplate.opsForList().leftPush(LIST_KEY, "item1");
        stringRedisTemplate.opsForList().leftPush(LIST_KEY, "item2"); // [item2, item1]

        stringRedisTemplate.opsForList().rightPush(LIST_KEY, "item3"); // [item2, item1, item3]

        Long size = stringRedisTemplate.opsForList().size(LIST_KEY);
        String first = stringRedisTemplate.opsForList().index(LIST_KEY, 0);
        String last = stringRedisTemplate.opsForList().index(LIST_KEY, -1);

        List<String> range = stringRedisTemplate.opsForList().range(LIST_KEY, 0, -1);

        // Then
        assertThat(size).isEqualTo(3);
        assertThat(first).isEqualTo("item2");
        assertThat(last).isEqualTo("item3");
        assertThat(range).containsExactly("item2", "item1", "item3");
    }

    @Test
    @Order(6)
    void should_Delete_Key() {
        // Given
        stringRedisTemplate.opsForValue().set(STRING_KEY, "will be deleted");

        // When
        Boolean deleted = stringRedisTemplate.delete(STRING_KEY);

        // Then
        assertThat(deleted).isTrue();
        assertThat(stringRedisTemplate.hasKey(STRING_KEY)).isFalse();
    }

    @Test
    @Order(7)
    void should_Use_Custom_RedisTemplate_With_Object() {
        // Given
        MyData data = new MyData("test-user", 25);

        // When
        redisTemplate.opsForValue().set("test:object", data, Duration.ofMinutes(1));
        MyData result = (MyData) redisTemplate.opsForValue().get("test:object");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("test-user");
        assertThat(result.getAge()).isEqualTo(25);
    }


    @Test
    void testUtil(){

        Boolean b = redisUtil.sIsMember("test", "ss");
        System.out.println(b.toString());
    }


    @Test
    void luaTest() {

        String key = "testlua";

        try {
            // 执行 Lua 脚本
            // Keys 和 Argv 的序列化方式由 RedisTemplate 的配置决定
            Long result = redisTemplate.execute(setWithExpireScript, Collections.singletonList(key), // KEYS 列表
                    "100",                          // ARGV[1]
                    3000L                          // ARGV[2]
            );
            // 根据脚本的返回值判断是否成功
            boolean f = result == 1L;
            assertThat(f).isTrue();
        } catch (Exception e) {
            log.error("", e);
        }

    }


    @Test
    void luaTest2() {

        String key1 = "coupon_count";
        String key2 = "coupon_user";
        List<String> keys = new ArrayList<>();
        keys.add(key1);
        keys.add(key2);

        String userId = "12466237";
        boolean f = false;
        try {
            // 执行 Lua 脚本
            // Keys 和 Argv 的序列化方式由 RedisTemplate 的配置决定
            Long result = redisTemplate.execute(couponDecr, keys, userId);
            // 根据脚本的返回值判断是否成功
            log.info("coupon_decr result: {}", result);
            f = result == 1L;

            assertThat(result).isEqualTo(2L);
        } catch (Exception e) {
            log.error("", e);

        } finally {
            assertThat(f).isFalse();

        }

    }





    // 辅助类，用于测试对象序列化
    public static class MyData {
        private String name;
        private int age;

        public MyData() {
        } // Jackson 需要无参构造

        public MyData(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "MyData{" + "name='" + name + '\'' + ", age=" + age + '}';
        }
    }
}
