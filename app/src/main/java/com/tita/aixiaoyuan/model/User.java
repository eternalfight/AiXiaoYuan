package com.tita.aixiaoyuan.model;

import cn.bmob.v3.BmobUser;
import com.tita.aixiaoyuan.Chat.bean.NewFriend;

/**
 * Created by zhangqie on 2016/11/26.
 */

public class User extends BmobUser {
    public User(){}
    private int errcode;
    private String errmsg;
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
    private String avatar;



    public User(NewFriend friend){
        setObjectId(friend.getUid());
        setUsername(friend.getName());
        setAvatar(friend.getAvatar());
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
