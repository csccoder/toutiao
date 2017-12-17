package com.nowcoder.model;

/**
 * @program: toutiao
 * @description:
 * @author: chenny
 * @create: 2017-12-14 22:59
 **/
public enum Entitype {
    NEWS(1),COMMENT(2);
    private int value;
    Entitype(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }

}
