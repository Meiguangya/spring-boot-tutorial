package com.mgy.springboottutorial.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * @author meiguangya
 * @date 2025/8/28 上午12:11
 * @description TODO
 */
@Data
public class Actor {

    private Integer actorId;
    private String firstName;
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private OffsetDateTime lastUpdate;

}
