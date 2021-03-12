package com.tita.aixiaoyuan.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class setLocationActivity extends AppCompatActivity {
    @BindView(R.id.location_et)
    EditText location_et;
    @BindView(R.id.save_btn)
    Button save_btn;
    private String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        location_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //EditText输入状态改变，Button背景颜色也改变
                if ("".equals(location_et.getText().toString().trim())) {
                    save_btn.setBackgroundResource(R.drawable.btn_gray);
                    save_btn.setEnabled(false);
                } else {
                    save_btn.setBackgroundResource(R.drawable.btn_green);
                    save_btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.btn_back,R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.save_btn:
                doSave();
                break;
        }
    }


    private void doSave() {
        if (location_et.getText().toString().trim().isEmpty()){
            ToastUtils.show("请输入宿舍地址");
        }else {
            location = location_et.getText().toString().trim();
            final User user =  BmobUser.getCurrentUser(User.class);
            user.setAddress(location);
            user.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        ToastUtils.show("保存成功");
                        finish();
                    }else{
                        ToastUtils.show("保存失败"+e.getMessage());
                    }
                }
            });
        }
    }

}