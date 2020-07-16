package com.leaf.zhsjalpha.bean;

public class User {
    public int code;
    public String msg;
    public String detail;

    @Override
    public String toString() {
        return "User{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
