package com.tita.aixiaoyuan.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tita.aixiaoyuan.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublishSuccessActivity extends AppCompatActivity {

    @BindView(R.id.pubsuess_back_btn)
    TextView pubsuess_back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_success);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.pubsuess_back_btn)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.pubsuess_back_btn:
                finish();
                break;
        }
    }

}