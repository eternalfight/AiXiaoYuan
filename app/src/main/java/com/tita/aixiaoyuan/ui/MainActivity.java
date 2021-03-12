package com.tita.aixiaoyuan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.andview.refreshview.XRefreshView;
import com.jude.utils.JUtils;
import com.orhanobut.logger.Logger;
import com.tita.aixiaoyuan.Adapter.HomePagerAdapter;
import com.tita.aixiaoyuan.Chat.event.RefreshEvent;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.fragment.CentreFragment;
import com.tita.aixiaoyuan.app.fragment.HomeFragment;
import com.tita.aixiaoyuan.app.fragment.MessageFragment;
import com.tita.aixiaoyuan.app.fragment.ShopCarFragment;
import com.tita.aixiaoyuan.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import static com.tita.aixiaoyuan.app.fragment.HomeFragment.STATUS_EXPANDED;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    XRefreshView refreshview;
    @BindView(R.id.view_pager)ViewPager mViewPager;
    @BindView(R.id.home_rb) RadioButton mHomeRb;
    @BindView(R.id.contents_rb) RadioButton mContentsRb;
    @BindView(R.id.message_rb) RadioButton mMessageRb;
    @BindView(R.id.center_rb) RadioButton mCenterRb;
    @BindView(R.id.iv_conversation_tips) ImageView iv_conversation_tips;
    @BindView(R.id.sale_btn) LinearLayout btn_sale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        JUtils.initialize(this.getApplication());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerview = findViewById(R.id.recycleview1);
        //refreshview = findViewById(R.id.refreshview);
        initData();
        //initChat();
    }


    private final void initData() {
        // 给ViewPager设置Adapter
        ArrayList<Fragment> fragments = new ArrayList<>();

        // 1.往集合里面添加Fragment
        fragments.add(new HomeFragment());
        fragments.add(new ShopCarFragment());
        fragments.add(new MessageFragment());
        fragments.add(new CentreFragment());

        // 2.适配器
        HomePagerAdapter homePagerAdapter =new HomePagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setOffscreenPageLimit(4);
        // 3.适配器 交给 ViewPager
        mViewPager.setAdapter(homePagerAdapter);

    }
/*    @Permission(permissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    }, requestCode = 100)
    public void requestPermission() {
        Toast.makeText(this, "权限请求成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, PermissionActivity.class);
        startActivity(intent);
    }

    @PermissionCancel
    public void requestPermissionCancel() {
        Toast.makeText(this, "权限请求取消", Toast.LENGTH_LONG).show();
    }

    @PermissionDenied
    public void requestPermissionDenied() {
        Toast.makeText(this, "权限请求被拒绝", Toast.LENGTH_LONG).show();
    }*/

    @OnClick({R.id.home_rb, R.id.contents_rb,R.id.message_rb,R.id.center_rb})
    public void onViewCheckedChanged(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId()) {
            case R.id.home_rb:
                // 把ViewPager切换到第一页
                mViewPager.setCurrentItem(0,false);// false 代表切换的时候不显示滑动效果
                break;
            case R.id.contents_rb:
                // 把ViewPager切换到第二页
                mViewPager.setCurrentItem(1,false);
                break;
            case R.id.message_rb:
                // 把ViewPager切换到第三页
                mViewPager.setCurrentItem(2,false);
                break;
            case R.id.center_rb:
                // 把ViewPager切换到第四页
                mViewPager.setCurrentItem(3,false);
                break;
        }
    }

    @OnPageChange(R.id.view_pager)
    public void onPageSelected(int position) {
        // 改变四个菜单的效果
        // 切换到相应页面之后调用  postion 当前的位置
        switch (position){
            case 0:
                mHomeRb.setChecked(true);
                mContentsRb.setChecked(false);
                mMessageRb.setChecked(false);
                mCenterRb.setChecked(false);
                break;
            case 1:
                mHomeRb.setChecked(false);
                mContentsRb.setChecked(true);
                mMessageRb.setChecked(false);
                mCenterRb.setChecked(false);
                break;
            case 2:
                mHomeRb.setChecked(false);
                mContentsRb.setChecked(false);
                mMessageRb.setChecked(true);
                mCenterRb.setChecked(false);
                break;
            case 3:
                mHomeRb.setChecked(false);
                mContentsRb.setChecked(false);
                mMessageRb.setChecked(false);
                mCenterRb.setChecked(true);
                break;
        }
    }
    @OnClick(R.id.sale_btn)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.sale_btn:
                Intent intent = new Intent(this, saleActivity.class);
                startActivityForResult(intent,0);
                break;
        }
    }

    private boolean isDragDown;
    private int mLastX = 0;
    private int mLastY = 0;
    private int mTouchSlop;


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isDragDown = false;
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if(Math.abs(deltaY) >= Math.abs(deltaX) && deltaY >=mTouchSlop){
                    isDragDown = true;
                }else{
                    isDragDown = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                mLastX = 0;
                mLastY = 0;
                isDragDown = false;
                break;
            default:
                break;
        }
        //canPtr(isDragDown);

        return super.dispatchTouchEvent(event);
    }

    /**
     * 默认展开状态
     */
    public static int mStatus = STATUS_EXPANDED;

    public void canPtr(boolean isDragDown){
        //展开 && recyclerView列表数据置顶 && 向下滑动时，可下来刷新
        //若正在刷新时，上滑，下拉刷新控件需要消耗事件，不往下传递事件
        boolean isCanPtr = isDragDown && HomeFragment.mStatus==STATUS_EXPANDED && isRecyclerTop();
        refreshview.disallowInterceptTouchEvent(!isCanPtr);
    }

    private boolean isRecyclerTop(){
        if(recyclerview!=null){
            RecyclerView.LayoutManager layoutManager = recyclerview.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerview.getChildAt(0);
                if (childAt == null || (firstVisibleItemPosition <= 1 && childAt.getTop() == 0)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //每次进来应用都检查会话和好友请求的情况
        //checkRedPoint();
        //进入应用后，通知栏应取消
        // BmobNotificationManager.getInstance(this).cancelNotification();
    }
    /**
     *
     */
    private void checkRedPoint() {
        //TODO 会话：4.4、获取全部会话的未读消息数量
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
        if (count > 0) {
            iv_conversation_tips.setVisibility(View.VISIBLE);
        } else {
            iv_conversation_tips.setVisibility(View.GONE);
        }
    }

    public void initChat(){
        final User user = BmobUser.getCurrentUser(User.class);
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getObjectId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getUsername(), user.getAvatar()));
                        EventBus.getDefault().post(new RefreshEvent());
                    } else {
                        JUtils.Toast(e.getMessage());
                    }
                }
            });
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    JUtils.Toast(status.getMsg());
                    Logger.i(BmobIM.getInstance().getCurrentStatus().getMsg());
                }
            });
        }
        //解决leancanary提示InputMethodManager内存泄露的问题
        //IMMLeaks.fixFocusedViewLeak(this.getApplication());
        //Permissions.checkStoragePermissions(0,MainActivity.this,this);
    }
    /**
     * 注册消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.3、通知有在线消息接收
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.4、通知有离线消息接收
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    //TODO 消息接收：8.5、通知有自定义消息接收
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        checkRedPoint();
    }




}
