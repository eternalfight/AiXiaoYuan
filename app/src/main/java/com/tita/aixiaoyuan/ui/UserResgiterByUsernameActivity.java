package com.tita.aixiaoyuan.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.jude.utils.JUtils;
import com.tita.aixiaoyuan.Chat.event.FinishEvent;
import com.tita.aixiaoyuan.Chat.model.BaseModel;
import com.tita.aixiaoyuan.Chat.model.UserModel;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.utils.CommUtil;
import org.greenrobot.eventbus.EventBus;

public class UserResgiterByUsernameActivity extends AppCompatActivity {

    @BindView(R.id.btn_back_login)
    TextView btnBackLogin;
    @BindView(R.id.user_phone_et1)
    EditText userPhoneEt1;
    @BindView(R.id.user_password_et1)
    EditText userPasswordEt1;
    @BindView(R.id.check_password_cb1)
    CheckBox checkPasswordCb;
    @BindView(R.id.user_password_et2)
    EditText userPasswordEt2;
    @BindView(R.id.check_password_cb2)
    CheckBox checkPasswordCb2;
    @BindView(R.id.user_register_bt1)
    Button userRegisterBt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_resgiter_by_username);
        Bmob.initialize(this, "e948069712e2bfa770ad378d9ce1ff73");
        ButterKnife.bind(this);
        JUtils.initialize(this.getApplication());
    }

    @OnClick({R.id.btn_back_login, R.id.user_register_bt1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back_login:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivityForResult(intent,0);
                finish();
                break;
            case R.id.user_register_bt1:
                DoRegister();
                break;
        }
    }
    @OnCheckedChanged({R.id.check_password_cb1,R.id.check_password_cb2})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.check_password_cb1:
                // 显示和隐藏密码
                if (isChecked) {
                    // 显示密码
                    userPasswordEt1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // 隐藏密码
                    userPasswordEt1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                // 光标移动到最后面
                CommUtil.cursorToEnd(userPasswordEt1);
                break;
            case R.id.check_password_cb2:
                // 显示和隐藏密码
                if (isChecked) {
                    // 显示密码
                    userPasswordEt2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // 隐藏密码
                    userPasswordEt2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                // 光标移动到最后面
                CommUtil.cursorToEnd(userPasswordEt2);
                break;
        }

    }

    private void showError(int e){
        String message;
        if (e==202){
            message = "用户名已经存在";
        }else if(e==9016) {
            message = "无网络连接，请检查您的手机网络.";
        }else if(e==9010){
            message = "网络超时";
        }else {
            message = "错误代码："+ e +"，请重试！";
        }
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("注册失败！")
                .setContentText(message)
                .show();
    }

    /**
     * 注册
     */
    public void DoRegister() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("请稍后");
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.getProgressHelper().setProgress(90);
        UserModel.getInstance().register(userPhoneEt1.getText().toString(), userPasswordEt1.getText().toString(), userPasswordEt2.getText().toString(), new LogInListener() {
            @Override
            public void done(Object o, BmobException e) {
                pDialog.getProgressHelper().setProgress(100);
                pDialog.hide();
                if (e == null) {
                    EventBus.getDefault().post(new FinishEvent());
                    //实现页面跳转
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                } else {
                    if (e.getErrorCode() == BaseModel.CODE_NOT_EQUAL) {
                        userPasswordEt2.setText("");
                    }
                    showError(e.getErrorCode());
                }
            }
        });
    }

}

