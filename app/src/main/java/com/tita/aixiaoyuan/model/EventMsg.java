package com.tita.aixiaoyuan.model;

import java.util.List;

public class EventMsg {
    private String msg;
    private List<String> stringList;

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
