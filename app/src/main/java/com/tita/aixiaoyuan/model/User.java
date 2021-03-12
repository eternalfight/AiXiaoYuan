package com.tita.aixiaoyuan.model;

import com.tita.aixiaoyuan.Chat.bean.NewFriend;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zhangqie on 2016/11/26.
 */

public class User extends BmobUser {
    public User(){}
    private int errcode;
    private String errmsg;
    private String nickname;
    private Integer age;
    private String sex;
    private String address;
    private String avatar;
    private BmobFile headAvatar;//头像
    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }




    public User(NewFriend friend){
        setObjectId(friend.getUid());
        setUsername(friend.getName());
        setAvatar(friend.getAvatar());
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public BmobFile getHeadAvatar() {
        return headAvatar;
    }

    public void setHeadAvatar(BmobFile headAvatar) {
        this.headAvatar = headAvatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
