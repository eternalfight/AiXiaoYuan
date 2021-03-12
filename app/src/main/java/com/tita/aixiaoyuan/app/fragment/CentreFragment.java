package com.tita.aixiaoyuan.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jude.utils.JUtils;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.widget.RecyclerViewFragment;
import com.tita.aixiaoyuan.model.User;
import com.tita.aixiaoyuan.ui.LoginActivity;
import com.tita.aixiaoyuan.ui.SettingActivity;
import com.tita.aixiaoyuan.ui.UserSettingActivity;
import com.tita.aixiaoyuan.ui.myDealActivity;
import com.tita.aixiaoyuan.ui.my_orderActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;

public class CentreFragment extends Fragment {


    @BindView(R.id.user_login_tv)
    TextView userLoginTv;
    @BindView(R.id.tv_myOrder)
    TextView tvMyOrder;
    @BindView(R.id.tv_waitForPay)
    TextView tvWaitForPay;
    @BindView(R.id.tv_waitForDeliver)
    TextView tvWaitForDeliver;
    @BindView(R.id.tv_waitForRecever)
    TextView tvWaitForRecever;
    @BindView(R.id.tv_waitForEvaluate)
    TextView tvWaitForEvaluate;
    @BindView(R.id.tv_AfterSale)
    TextView tvAfterSale;
    @BindView(R.id.tv_Alltransaction)
    TextView tvAlltransaction;
    @BindView(R.id.tv_IPublished)
    TextView tvIPublished;
    @BindView(R.id.tv_ISoldThem)
    TextView tvISoldThem;
    @BindView(R.id.tv_center_setting)
    TextView tvCenterSetting;
    @BindView(R.id.tv_publem)
    TextView tvPublem;
    @BindView(R.id.tv_serverCenter)
    TextView tvServerCenter;
    @BindView(R.id.user_logined_ll)
    LinearLayout user_logined_ll;
    @BindView(R.id.user_head_iv)
    ImageView user_head_iv;//头像
    @BindView(R.id.user_name_tv)
    TextView user_name_tv;//用户名
    @BindView(R.id.user_location_tv)
    TextView user_location_tv; //地理位置
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center, null);
        //返回一个Unbinder值（进行解绑），注意这里的this不能使用getActivity()
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private List<Fragment> getPageFragments() {
        List<Fragment> data = new ArrayList<>();
        data.add(new RecyclerViewFragment());
        data.add(new RecyclerViewFragment());
        data.add(new RecyclerViewFragment());
        return data;
    }


    @Override
    public void onResume() {
        super.onResume();
        // 判断用户是否登录，如果登录了显示登录的中心头部，否则显示未登录的中心头部
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            user_logined_ll.setVisibility(View.VISIBLE);
            userLoginTv.setVisibility(View.GONE);
            String nickname = (String) BmobUser.getObjectByKey("nickname");
            String addr = (String) BmobUser.getObjectByKey("address");
            user_name_tv.setText(nickname);
            user_location_tv.setText(addr);
            Log.i("TAG", "onResume: "+ addr);
            fetchUserInfo(getView());
            showHeadImg();
        } else {
            userLoginTv.setVisibility(View.VISIBLE);
            user_logined_ll.setVisibility(View.GONE);
        }

    }
    private void fetchUserInfo(final View view) {
        BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    final User myUser = BmobUser.getCurrentUser(User.class);
                    JUtils.Toast("更新用户本地缓存信息成功");
                } else {
                    Log.e("error",e.getMessage());
                    JUtils.Toast("更新用户本地缓存信息失败：" + e.getMessage());
                }
            }
        });
    }
    public void showHeadImg(){

        //下载图片
        BmobQuery<User> query=new BmobQuery<User>();
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e == null){
                    show_ad(list);
                }else{
                    JUtils.Toast(""+e.getMessage());
                }
            }
        });
    }
    public void show_ad(List<User> list){
        User myUser = BmobUser.getCurrentUser(User.class);
        Log.i("Username", "getCurrentUser: "+ myUser.getUsername());

        for (User ad : list){
            Log.i("Username", "show_ad: "+ad.getUsername());
            if(ad.getUsername() != null && myUser.getUsername().equals(ad.getUsername())){
                BmobFile icon= ad.getHeadAvatar();
                icon.download(new DownloadFileListener() {
                    @Override
                    public void onProgress(Integer integer, long l) {
                        Log.i("Username", "onProgress "+ integer);
                    }
                    @Override
                    public void done(String s, BmobException e) {
                        if(e == null){
                            Log.i("Username", "uri: "+ s);
                            user_head_iv.setImageBitmap(BitmapFactory.decodeFile(s)); //根据地址解码并显示图片
                        }else {
                            Log.i("Username", "BmobException: "+ e.getMessage());
                        }
                    }
                });
                break;
            }
        }

    }
    @OnClick({R.id.user_login_tv, R.id.tv_myOrder, R.id.tv_waitForPay, R.id.tv_waitForDeliver, R.id.tv_waitForRecever, R.id.tv_waitForEvaluate,
            R.id.tv_AfterSale, R.id.tv_Alltransaction, R.id.tv_IPublished, R.id.tv_ISoldThem, R.id.tv_center_setting, R.id.tv_publem,
            R.id.tv_serverCenter,R.id.user_head_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_login_tv:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.tv_myOrder:
                start_myorder_Activity(0);
                break;
            case R.id.tv_waitForPay:
                start_myorder_Activity(1);
                break;
            case R.id.tv_waitForDeliver:
                start_myorder_Activity(2);
                break;
            case R.id.tv_waitForRecever:
                start_myorder_Activity(3);
                break;
            case R.id.tv_waitForEvaluate:
                start_myorder_Activity(4);
                break;
            case R.id.tv_AfterSale:
                break;
            case R.id.tv_Alltransaction:
                start_mydeal_Activity(0);
                break;
            case R.id.tv_IPublished:
                start_mydeal_Activity(0);
                break;
            case R.id.tv_ISoldThem:
                start_mydeal_Activity(1);
                break;
            case R.id.tv_center_setting:
                Intent intent1 = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent1,0);
                break;
            case R.id.tv_publem:

                break;
            case R.id.tv_serverCenter:
                break;
            case  R.id.user_head_iv:
                Intent intent2 = new Intent(getActivity(), UserSettingActivity.class);
                startActivityForResult(intent2,0);
                break;
        }
    }
    public void start_myorder_Activity(int page){
        Intent intent1 = new Intent(getActivity(), my_orderActivity.class);
        intent1.putExtra("fragment_flag", page);
        startActivityForResult(intent1,0);
    }
    public void start_mydeal_Activity(int page){
        Intent intent1 = new Intent(getActivity(), myDealActivity.class);
        intent1.putExtra("fragment_flag", page);
        startActivityForResult(intent1,0);
    }
}
