package com.tita.aixiaoyuan.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.widget.RecyclerViewFragment;
import com.tita.aixiaoyuan.ui.LoginActivity;
import com.tita.aixiaoyuan.ui.SettingActivity;
import com.tita.aixiaoyuan.ui.my_orderActivity;

import java.util.ArrayList;
import java.util.List;

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
    }

    @OnClick({R.id.user_login_tv, R.id.tv_myOrder, R.id.tv_waitForPay, R.id.tv_waitForDeliver, R.id.tv_waitForRecever, R.id.tv_waitForEvaluate, R.id.tv_AfterSale, R.id.tv_Alltransaction, R.id.tv_IPublished, R.id.tv_ISoldThem, R.id.tv_center_setting, R.id.tv_publem, R.id.tv_serverCenter})
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
                break;
            case R.id.tv_IPublished:
                break;
            case R.id.tv_ISoldThem:
                break;
            case R.id.tv_center_setting:
                Intent intent1 = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent1,0);
                break;
            case R.id.tv_publem:
                break;
            case R.id.tv_serverCenter:
                break;
        }
    }
    public void start_myorder_Activity(int page){
        Intent intent1 = new Intent(getActivity(), my_orderActivity.class);
        intent1.putExtra("fragment_flag", page);
        startActivityForResult(intent1,0);
    }
}
