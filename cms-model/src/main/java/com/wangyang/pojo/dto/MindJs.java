package com.wangyang.pojo.dto;

import lombok.Data;

@Data
public class MindJs {
    private String id;
    private String topic;
    private Boolean expanded;
    private Integer parentid;
    private String direction;
    private Integer order;

}
