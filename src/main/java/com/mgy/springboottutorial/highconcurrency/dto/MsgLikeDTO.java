package com.mgy.springboottutorial.highconcurrency.dto;

import lombok.Data;

import java.util.List;

@Data
public class MsgLikeDTO {

    private Integer userId;
    private String msgId;

    // 1-like 0-dislike
    private Integer like;


    @Data
    public static class MsgCountQryDTO{

        List<String> msgIds;

    }

    @Data
    public static class MsgCountResp{

        private Long count;
        private String msgId;

    }



}
