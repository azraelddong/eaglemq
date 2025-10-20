package com.infoepoch.cmgs.coder;

import com.infoepoch.cmgs.constants.BrokerConstants;
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

    public TcpMsg(int code, byte[] data) {
        this.magic = BrokerConstants.DEFAULT_MAGIC_NUM;
        this.code = code;
        this.len = data.length;
        this.data = data;
    }
}
