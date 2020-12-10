package com.tita.aixiaoyuan.Chat.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.tita.aixiaoyuan.R;


public class SetPermissionDialog extends Dialog implements View.OnClickListener {

     private Context context;
     private Button mLeftButton, mRightButton;
     private OnConfirmCancelClickListener mConfirmCancelClickListener;

    public SetPermissionDialog(Context context) {
        super(context, R.style.common_my_dialog_style);
        this.context = context;
        //    this.buttonListener = buttonListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_permission);
        initView();

    }

    public void initView() {

        mLeftButton = (Button) findViewById(R.id.btn_left);
        mRightButton = (Button) findViewById(R.id.btn_right);

        getWindow().setWindowAnimations(R.style.common_my_dialog_style);



        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_left) {
            if (mConfirmCancelClickListener != null) {
                mConfirmCancelClickListener.onLeftClick();
            }
        } else if (v.getId() == R.id.btn_right) {
            if (mConfirmCancelClickListener != null) {
                mConfirmCancelClickListener.onRightClick();
            }
        }
    }

    public SetPermissionDialog setConfirmCancelListener (OnConfirmCancelClickListener listener) {
        mConfirmCancelClickListener = listener;
        return this;
    }

    public static interface OnConfirmCancelClickListener {
        public void onLeftClick();
        public void onRightClick();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        }
        return false;
    }
}
