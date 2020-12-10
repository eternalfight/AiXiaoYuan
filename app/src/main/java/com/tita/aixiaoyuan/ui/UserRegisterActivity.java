package com.tita.aixiaoyuan.ui;

import android.content.Intent;
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
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.widget.VerificationCodeButton;
import com.tita.aixiaoyuan.utils.CommUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class UserRegisterActivity extends AppCompatActivity {

    @BindView(R.id.btn_back_login)
    TextView btnBackLogin;
    @BindView(R.id.user_phone_et)
    EditText mUserPhoneEt;
    @BindView(R.id.user_code_et)
    EditText mUserCodeEt;
    @BindView(R.id.send_code_bt)
    VerificationCodeButton mSendCodeBt;
    @BindView(R.id.user_password_et)
    EditText mUserPasswordEt;
    @BindView(R.id.check_password_cb)
    CheckBox mCheckPasswordCb;
    @BindView(R.id.user_register_bt)
    Button mUserRegisterBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        ButterKnife.bind(this);
        JUtils.initialize(this.getApplication());
    }

    @OnClick({R.id.btn_back_login, R.id.send_code_bt, R.id.user_register_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back_login:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.send_code_bt:
                requestUserCode();
                break;
            case R.id.user_register_bt:
                dealUserRegister();
                break;
        }
    }

    private void requestUserCode() {
    }

    private void dealUserRegister() {
    }

    @OnCheckedChanged(R.id.check_password_cb)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 显示和隐藏密码
        if (isChecked) {
            // 显示密码
            mUserPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // 隐藏密码
            mUserPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // 光标移动到最后面
        CommUtil.cursorToEnd(mUserPasswordEt);
    }
}
