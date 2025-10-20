package com.infoepoch.cmgs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventCodeEnum {
    REGISTRY(1, "上线"),
    UN_REGISTRY(2, "下线"),
    HEART_BEAT(3, "心跳");

    private int code;
    private String desc;
}
