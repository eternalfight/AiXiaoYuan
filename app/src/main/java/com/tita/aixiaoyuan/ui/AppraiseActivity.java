package com.tita.aixiaoyuan.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;
import com.tita.aixiaoyuan.R;

import butterknife.BindView;
import butterknife.OnClick;

public class AppraiseActivity extends AppCompatActivity {
    @BindView(R.id.goods_titel)
    TextView goods_titel;
    @BindView(R.id.good_appraise)
    EditText good_appraise;
    private int flag = 0; //评论内容是否为空标志  0-空 1-不空
    private String GoodsTitle;
    private String GoodsAppraise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise);
    }

    public void init(){
        good_appraise.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(good_appraise.getText())) {
                    good_appraise.setBackgroundResource(R.drawable.bg_btn_yelow);
                    flag = 1;
                    return false;
                }
                return false;
            }
        });
    }
    @OnClick({R.id.back_btn,R.id.btn_publish})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                finish();
            case R.id.btn_publish:
                doPublish();
        }

    }

    private void doPublish() {
        if (flag == 1){
            GoodsAppraise = good_appraise.getText().toString().trim();
        }else {
            ToastUtils.show("内容不能为空");
        }
    }


}