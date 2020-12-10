package com.tita.aixiaoyuan.Chat.model.i;

import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;
import com.tita.aixiaoyuan.model.User;

/**
 * @author :smile
 * @project:QueryUserListener
 * @date :2016-02-01-16:23
 */
public abstract class QueryUserListener extends BmobListener1<User> {

    public abstract void done(User s, BmobException e);

    @Override
    protected void postDone(User o, BmobException e) {
        done(o, e);
    }
}
