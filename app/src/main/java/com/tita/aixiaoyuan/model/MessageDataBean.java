package com.tita.aixiaoyuan.model;

public class MessageDataBean {
    private String usernames;
    private int Icns;
    private String Times;
    private String LastMessage;

    public String getUsernames() {
        return usernames;
    }

    public void setUsernames(String usernames) {
        this.usernames = usernames;
    }

    public int getIcns() {
        return Icns;
    }

    public void setIcns(int icns) {
        Icns = icns;
    }

    public String getTimes() {
        return Times;
    }

    public void setTimes(String times) {
        Times = times;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }
}
