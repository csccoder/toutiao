package com.nowcoder.async;

public enum  EventType {
    LIKE(1),COMMENT(2),LOGIN(3),REGISTER(4);
    private int value;
    EventType(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }
}
