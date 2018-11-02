package com.gsq.nio.demo.message;

/**
 * @author guishangquan
 * @date 2018/10/31
 */
public enum MessageType {
    TEXT(1),
    FILE(2),
    ;

    private int type;

    MessageType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
