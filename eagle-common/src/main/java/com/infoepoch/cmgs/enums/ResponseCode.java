package com.infoepoch.cmgs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    ERROR_USER_PWD(1, "用户密码错误"),
    UN_REGISTRY(2, "下线");

    private int code;
    private String desc;
}
