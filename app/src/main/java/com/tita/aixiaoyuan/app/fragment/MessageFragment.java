package com.tita.aixiaoyuan.app.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tita.aixiaoyuan.Adapter.messageAdapter;
import com.tita.aixiaoyuan.Chat.activity.ChatActivity;
import com.tita.aixiaoyuan.Chat.widget.SetPermissionDialog;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.widget.BubblePopupView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MessageFragment extends Fragment {

    private float X,Y;
    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
    @BindView(R.id.message_id)
    TextView textView;
    //@BindView(R.id.Message_recycleview)
    RecyclerView mRecycleVIew;
    private messageAdapter mAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int position = 0;

        List<String> popupItemList = new ArrayList<String>();
        popupItemList.add("删除");
        View view = inflater.inflate(R.layout.fragment_message,null);
        ButterKnife.bind(this, view);
        mRecycleVIew = view.findViewById(R.id.Message_recycleview);
        mRecycleVIew.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new messageAdapter();
        mRecycleVIew.setAdapter(mAdapter);
        mAdapter.addData();
        mAdapter.setOnItemClickListener(new messageAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                startActivity(intent);
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestPermisson();
                    }
                }, 100);*/
                Toast.makeText(getActivity(), "click " + position, Toast.LENGTH_SHORT).show();
            }
        });


        mAdapter.setOnTouchListener(new messageAdapter.OnTouchListener() {
            @Override
            public void onTouch(float mRawX, float mRawY) {
                X = mRawX;
                Y = mRawY;
            }
        });

        mAdapter.setOnItemLongClickListener(new messageAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position, int[] location) {
                //实例化BubblePopupView
                BubblePopupView bubblePopup = new BubblePopupView(view.getContext());
                //是否跟随手指显示，默认false，设置true后翻转高度无效，永远在上方显示
                bubblePopup.setShowTouchLocation(true);
                //要实现顶部气泡向下，需要添加翻转高度，默认为0不翻转（举例：导航栏高度40dp，列表item高度40dp,需设置40+40=80）
                bubblePopup.setmReversalHeight(0f);

                bubblePopup.showPopupListWindow(view, position, X, Y, popupItemList, new BubblePopupView.PopupListListener() {
                    @Override
                    public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                        return true;
                    }
                    @Override
                    public void onPopupListClick(View contextView, int contextPosition, int position) {
                        //Toast.makeText(contextView.getContext(), contextPosition + "," + position, Toast.LENGTH_SHORT).show();
                        mAdapter.removeData(contextPosition);
                       // Log.i("Tag", "onClick:  position:" + contextPosition);
                    }
                });

            }
        });
        //返回一个Unbinder值（进行解绑），注意这里的this不能使用getActivity()
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void requestPermisson(){
        RxPermissions rxPermission = new RxPermissions(getActivity());
        rxPermission
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                )
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            startActivity(new Intent(getActivity(),ChatActivity.class));
                        } else {

                            SetPermissionDialog mSetPermissionDialog = new SetPermissionDialog(getActivity());
                            mSetPermissionDialog.show();
                            mSetPermissionDialog.setConfirmCancelListener(new SetPermissionDialog.OnConfirmCancelClickListener() {
                                @Override
                                public void onLeftClick() {
                                    //getActivity().finish();
                                }

                                @Override
                                public void onRightClick() {
                                    //getActivity().finish();
                                }
                            });
                        }
                    }
                });
    }
}
