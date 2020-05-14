package com.wangyang.weixin.pojo;

import lombok.Data;

@Data
public class TextMessage {
    private String ToUserName;

    private String FromUserName;
    private String CreateTime;
    private String MsgType;

    private String Content;
    private String MsgId;
}
