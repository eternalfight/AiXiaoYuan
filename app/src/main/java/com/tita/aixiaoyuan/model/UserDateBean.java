package com.tita.aixiaoyuan.model;

import cn.bmob.v3.BmobObject;

public class UserDateBean extends BmobObject {
    private String UserName;
    private String Password;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
