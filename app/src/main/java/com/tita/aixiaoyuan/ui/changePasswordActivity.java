package com.tita.aixiaoyuan.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;
import com.jude.utils.JUtils;
import com.tita.aixiaoyuan.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class changePasswordActivity extends AppCompatActivity {

    @BindView(R.id.OldPassword)
    EditText oldPassword;
    @BindView(R.id.NewPassword)
    EditText newPassword;
    private String oldPwd;
    private String newPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.summit,R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.summit:
                getPassword(view);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void getPassword(View view) {
        oldPwd = oldPassword.getText().toString().trim();
        newPwd = newPassword.getText().toString().trim();
        if (oldPwd.isEmpty()){
            JUtils.Toast("请输入原密码");
        }else if (newPwd.isEmpty()){
            JUtils.Toast("请输入新密码");
        }else {
            updatePassword(view);
        }
    }


    /**
     * 提供旧密码修改密码
     */
    private void updatePassword(final View view){
        //TODO 此处替换为你的旧密码和新密码
        BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.show("修改成功");
                } else {
                    ToastUtils.show("修改失败"+ e.getMessage());
                }
            }
        });
    }
}