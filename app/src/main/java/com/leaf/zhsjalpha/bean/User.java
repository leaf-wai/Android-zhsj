package com.leaf.zhsjalpha.bean;

public class User {
    public int code;
    public String msg;
    public String detail;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "User{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
