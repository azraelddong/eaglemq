package com.infoepoch.cmgs.coder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TcpMsg {

    private short magic;

    private int code;

    private int len;

    private byte[] data;
}
