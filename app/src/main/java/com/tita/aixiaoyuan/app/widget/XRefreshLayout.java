package com.tita.aixiaoyuan.app.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.callback.IHeaderCallBack;
import com.andview.refreshview.utils.Utils;
import com.tita.aixiaoyuan.R;

import java.util.Calendar;

public class XRefreshLayout extends LinearLayout implements IHeaderCallBack {
    private ViewGroup mContent;
    private ImageView mArrowImageView;
    private CircleProgress mProgressBar;
    private TextView mHintTextView;
    private TextView mHeaderTimeTextView;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private final int ROTATE_ANIM_DURATION = 180;
    private Context mContext;
    private Animation circleAnim;
    private boolean isRefreshing = false;
    /**
     * 下拉刷新的图片控件
     */
    private ImageView mRefrushImageView;

    public XRefreshLayout(Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContent = (ViewGroup) LayoutInflater.from(context).inflate(
                R.layout.xlistview_header, this);
        mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
        mHeaderTimeTextView = (TextView) findViewById(R.id.xlistview_header_last_time_textview);
        mProgressBar = (CircleProgress) findViewById(R.id.circle_progress);
        mRefrushImageView = (ImageView) findViewById(R.id.xlistview_refrush_img);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

        circleAnim = AnimationUtils.loadAnimation(context, R.anim.refreshing_progress_bar_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();
        circleAnim.setInterpolator(interpolator);
    }

    @Override
    public void setRefreshTime(long lastRefreshTime) {

        // 获取当前时间
        Calendar mCalendar = Calendar.getInstance();
        long refreshTime = mCalendar.getTimeInMillis();
        long howLong = refreshTime - lastRefreshTime;
        int minutes = (int) (howLong / 1000 / 60);
        String refreshTimeText = null;
        Resources resources = getContext().getResources();
        if (minutes < 1) {
            refreshTimeText = resources
                    .getString(com.andview.refreshview.R.string.xrefreshview_refresh_justnow);
        } else if (minutes < 60) {
            refreshTimeText = resources
                    .getString(com.andview.refreshview.R.string.xrefreshview_refresh_minutes_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes);
        } else if (minutes < 60 * 24) {
            refreshTimeText = resources
                    .getString(com.andview.refreshview.R.string.xrefreshview_refresh_hours_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes / 60);
        } else {
            refreshTimeText = resources
                    .getString(com.andview.refreshview.R.string.xrefreshview_refresh_days_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes / 60 / 24);
        }
        mHeaderTimeTextView.setText(refreshTimeText);
    }

    /**
     * hide footer when disable pull load more
     */
    @Override
    public void hide() {
        setVisibility(View.GONE);
    }

    @Override
    public void show() {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateNormal() {
        mProgressBar.clearAnimation();
        isRefreshing = false;

        getHeaderRefrushImg();
    }

    @Override
    public void onStateReady() {
        isRefreshing = false;
        mProgressBar.setBackgroundResource(R.drawable.refresh_circular);
    }

    @Override
    public void onStateRefreshing() {
        isRefreshing = true;
        mProgressBar.startAnimation(circleAnim);
    }

    @Override
    public void onStateFinish(boolean success) {
        isRefreshing = false;
    }


    @Override
    public void onHeaderMove(double offset, int offsetY, int deltaY) {

        if (!isRefreshing) {
            if (mProgressBar.getAnimation() == null) {
                mProgressBar.setRotation((int) ((-1) * offsetY));
            }
        }
    }

    @Override
    public int getHeaderHeight() {
        return mContent.getMeasuredHeight();
    }

    public void getHeaderRefrushImg() {
        Bitmap refrushBitmap = null;
        if (refrushBitmap != null) {
            mRefrushImageView.setImageBitmap(refrushBitmap);
        } else {
            mRefrushImageView.setImageResource(R.drawable.refresh_word);
        }
    }

    public boolean isRefreshing(){
        return isRefreshing;
    }
}
