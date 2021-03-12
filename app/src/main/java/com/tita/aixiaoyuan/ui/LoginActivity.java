package com.tita.aixiaoyuan.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jude.utils.JUtils;
import com.tita.aixiaoyuan.Chat.model.UserModel;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.utils.CommUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.user_login_bt) Button mUserLoginBt; // 登录按钮
    @BindView(R.id.check_password_cb) CheckBox mCheckPasswordCb; // 隐藏 显示 密码的 密文/明文
    @BindView(R.id.user_phone_et)  EditText mUserPhoneEt; // 手机号  密码  输入框
    @BindView(R.id.user_password_et)  EditText mUserPasswordEt;
    @BindView(R.id.user_register_tv) TextView mUserRegisterTv; // 跳转到注册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        JUtils.initialize(this.getApplication());
        ButterKnife.bind(this);
        Bmob.initialize(this, "e948069712e2bfa770ad378d9ce1ff73");
        // 1.完成基本功能   显示和隐藏密码
        // 监听CheckBox状态改变
        mCheckPasswordCb.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 显示和隐藏密码
        if (isChecked){
            // 显示密码
            mUserPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else {
            mUserPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // 光标移动到最后面
        CommUtil.cursorToEnd(mUserPasswordEt);
    }

    @OnClick({R.id.user_login_bt,R.id.user_register_tv})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_login_bt:
                DoLoginClick();
                break;
            case R.id.user_register_tv:
                // 跳转到注册界面
                Intent intent = new Intent(this,UserResgiterByUsernameActivity.class);
                startActivityForResult(intent,0);
                finish();
                break;
        }
    }
    /**
     * 登录
     *
     * @param
     */
    public void DoLoginClick() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("登录中");
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.getProgressHelper().setProgress(90);
        UserModel.getInstance().login(mUserPhoneEt.getText().toString().trim(), mUserPasswordEt.getText().toString().trim(), new LogInListener() {
            @Override
            public void done(Object o, BmobException e) {
                pDialog.getProgressHelper().setProgress(100);
                if (e == null) {
                    //登录成功
                    //进入主界面
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    JUtils.Toast("登录成功");
                    pDialog.dismiss();
                    finish();
                } else {
                    JUtils.Toast("登录失败：" + e.getMessage() + ",错误代码：" + e.getErrorCode());
                    pDialog.dismiss();
                }
            }
        });
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
