package com.tita.aixiaoyuan.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.tita.aixiaoyuan.Adapter.ChooseGoodsAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.fragment.ShopMainFragment;
import com.tita.aixiaoyuan.app.widget.CustomLinearLayoutManager;
import com.tita.aixiaoyuan.model.ThingsDetails;
import com.tita.aixiaoyuan.utils.BigDecimalUtils;
import com.ycbjie.slide.LoggerUtils;
import com.ycbjie.slide.SlideAnimLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopDetialActivity extends AppCompatActivity implements ShopMainFragment.SendMessageCommunitor {
    private SlideAnimLayout mSlideDetailsLayout;
    private ShopMainFragment shopMainFragment;
    private WebView webView;
    private LinearLayout ll_page_more;
    private ImageView mIvMoreImg;
    private TextView mTvMoreText;
    private TextView tvBarGoods;
    private TextView tvBarDetail;
    private LinearLayout root;
    private NestedScrollView scrollView;

    @BindView(R.id.back_btn) TextView back_btn;
    @BindView(R.id.addtocontent) TextView addtocontent;


    private ThingsDetails thingsDetails;//获取商品详情
    private int countNum = 1;//购买商品数量
    private int sum = 0;//购买商品库存
    private String itemName;//选择的商品名
    private String itemNameId;//选择的商品名
    private int positionNow = 0;//当前规格位置
    private HashMap<Integer, String> map;
    private String jsonString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detial);
        ButterKnife.bind(this);
        initJsonData();
        getData();
        initView();
        initListener();
        initShopMainFragment();
        initSlideDetailsLayout();
        initWebView();
    }
    private void initView() {
        root = findViewById(R.id.root);
        mSlideDetailsLayout = findViewById(R.id.slideDetailsLayout);
        ll_page_more = findViewById(R.id.ll_page_more);
        webView = findViewById(R.id.wb_view);
        mIvMoreImg = findViewById(R.id.iv_more_img);
        mTvMoreText =  findViewById(R.id.tv_more_text);
        tvBarGoods = findViewById(R.id.tv_bar_goods);
        tvBarDetail = findViewById(R.id.tv_bar_detail);
        scrollView = findViewById(R.id.scrollView);
    }

    private void initListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_bar_goods:
                        tvBarGoods.setTextColor(Color.RED);
                        tvBarDetail.setTextColor(Color.BLACK);
                        mSlideDetailsLayout.smoothClose(true);
                        break;
                    case R.id.tv_bar_detail:
                        tvBarGoods.setTextColor(Color.BLACK);
                        tvBarDetail.setTextColor(Color.RED);
                        mSlideDetailsLayout.smoothOpen(true);
                        break;
                    default:
                        break;
                }
            }
        };
        tvBarGoods.setOnClickListener(onClickListener);
        tvBarDetail.setOnClickListener(onClickListener);
    }


    private void initShopMainFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(shopMainFragment==null){
            shopMainFragment = new ShopMainFragment();
            fragmentTransaction
                    .replace(R.id.fl_shop_main2, shopMainFragment)
                    .commit();
        }else {
            fragmentTransaction.show(shopMainFragment);
        }
    }

    @OnClick({R.id.back_btn,R.id.buyNow,R.id.addtocontent})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.buyNow:
                showShopDialog();
                break;
            case R.id.addtocontent:
                showShopDialog();
                break;
            case R.id.ll_current_goods:

                break;
        }
    }

    private void showShopDialog() {
        map = new HashMap<>();
        final Dialog mDialog = new Dialog(this, R.style.DialogWindowStyle_Shop_bg);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.shop_detial_dialog, (ViewGroup) findViewById(android.R.id.content), false);
        mDialog.setContentView(dialogView);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        Window localWindow = mDialog.getWindow();
        localWindow.setWindowAnimations(R.style.DialogWindowStyles);
        localWindow.setGravity(Gravity.BOTTOM);
        localWindow.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = localWindow.getAttributes();
        int[] wh = {ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT};
        lp.width = wh[0];
        lp.height = wh[1];
        localWindow.setAttributes(lp);

        if (mDialog != null && !mDialog.isShowing())
            mDialog.show();
        final ImageView ivImage = dialogView.findViewById(R.id.iv_image);
        final TextView tvMoney = dialogView.findViewById(R.id.tv_money);
        final TextView tvLeaveNumber = dialogView.findViewById(R.id.tv_leave_number);
        LinearLayout llClose = dialogView.findViewById(R.id.ll_close);
        final RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        ImageView ivSub = dialogView.findViewById(R.id.iv_sub);
        final EditText etGoodNum = dialogView.findViewById(R.id.et_good_num);
        ImageView ivAdd = dialogView.findViewById(R.id.iv_add);
        final Button btnSure = dialogView.findViewById(R.id.btn_sure);

        sum = thingsDetails.getNumber();
        tvMoney.setText("¥ " + BigDecimalUtils.toDecimal(thingsDetails.getPlatform_price(), 2));
        tvLeaveNumber.setText("库存" + thingsDetails.getNumber() + "件");

         //****************************选择商品规格（开始）****************************************//
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(mLayoutManager);
        final ChooseGoodsAdapter chooseGoodsAdapter = new ChooseGoodsAdapter();
        recyclerView.setAdapter(chooseGoodsAdapter);
        chooseGoodsAdapter.addData(thingsDetails.getSpecTile());
        //****************************初始化数据**********************************/
        for (int i = 0; i < thingsDetails.getSpecTile().size(); i++) {
            for (int j = 0; j < thingsDetails.getSpecTile().get(i).getValues().size(); j++) {
                thingsDetails.getSpecTile().get(i).getValues().get(j).setCanSelect(true);
            }
        }
        //******************开始匹配前再把所有没货的id挑出来(开始)******************/
        List<String> allIDList = new ArrayList<>();
        List<String> allIDList1 = new ArrayList<>();
        List<String> allList = new ArrayList<>();
        for (int n = 0; n < thingsDetails.getSpecList().size(); n++) {
            for (int i = 0; i < thingsDetails.getSpecTile().size(); i++) {
                for (int j = 0; j < thingsDetails.getSpecTile().get(i).getValues().size(); j++) {
                    allList.add(thingsDetails.getSpecTile().get(i).getValues().get(j).getId());
                    if (thingsDetails.getSpecList().get(n).getSpec_value_ids().contains(thingsDetails.getSpecTile().get(i).getValues().get(j).getId())) {
                        allIDList.add(thingsDetails.getSpecList().get(n).getSpec_value_ids());
                    }
                }
            }
        }
        for (int i = 0; i < allIDList.size(); i++) {
            List<String> list = Arrays.asList(allIDList.get(i).split(","));
            for (String s : list) {
                allIDList1.add(s);
            }
        }
        allList.removeAll(allIDList1);
        for (int n = 0; n < allList.size(); n++) {
            for (int i = 0; i < thingsDetails.getSpecTile().size(); i++) {
                for (int j = 0; j < thingsDetails.getSpecTile().get(i).getValues().size(); j++) {
                    if (allList.get(n).contains(thingsDetails.getSpecTile().get(i).getValues().get(j).getId())) {
                        thingsDetails.getSpecTile().get(i).getValues().get(j).setCanSelect(false);
                    }
                }
            }
        }
        //****************开始匹配前再把所有没货的id挑出来(结束)*********************/
        chooseGoodsAdapter.setTagItemOnClickListener(new ChooseGoodsAdapter.TagItemOnClick() {
            @Override
            public void onItemClick(View view, int positions, int position) {
                //*********************布局切换相关(开始)*********************/
                if (thingsDetails.getSpecTile().get(position).getValues().get(positions).isCanSelect()) {
                    if (thingsDetails.getSpecTile().get(position).getValues().get(positions).isSelect()) {
                        map.remove(position);
                        thingsDetails.getSpecTile().get(position).getValues().get(positions).setSelect(false);
                    } else {
                        map.put(position, thingsDetails.getSpecTile().get(position).getValues().get(positions).getId());
                        for (int i = 0; i < thingsDetails.getSpecTile().get(position).getValues().size(); i++) {
                            thingsDetails.getSpecTile().get(position).getValues().get(i).setSelect(false);
                        }
                        thingsDetails.getSpecTile().get(position).getValues().get(positions).setSelect(true);
                    }
                    //*********************布局切换相关(结束)*********************/
                    if (map.size() == chooseGoodsAdapter.getItemCount()) {
                        Log.v("规格", "开始匹配");
                        String ids = "";
                        String ids1 = "";
                        for (int i = 0; i < map.size(); i++) {
                            ids += map.get(i) + ",";
                        }
                        if (map.size() >= 1) {
                            for (int i = map.size() - 1; i >= 0; i--) {
                                ids1 += map.get(i) + ",";
                            }
                        }
                        boolean isHave = false;
                        for (int i = 0; i < thingsDetails.getSpecList().size(); i++) {
                            String idString = thingsDetails.getSpecList().get(i).getSpec_value_ids();
                            if (ids.contains(idString) || ids1.contains(idString)) {
                                isHave = true;
                                Log.v("规格", "匹配到了");
//                                Utils.setImageViewSigle(thingsDetails.getSpecList().get(i).getPic(), ivImage, mContext);
                                tvMoney.setText("¥ " + BigDecimalUtils.toDecimal(thingsDetails.getSpecList().get(i).getPlatform_price(), 2));
                                tvLeaveNumber.setText("库存" + thingsDetails.getSpecList().get(i).getNumber() + "件");
                                ((TextView) dialogView.findViewById(R.id.tv_have_choose)).setText("已选择 " + thingsDetails.getSpecList().get(i).getSpec_value_texts());
                                sum = thingsDetails.getSpecList().get(i).getNumber();
                            }
                        }
                        if (!isHave) {
//                            Utils.setImageViewSigle(thingsDetails.getPics(), ivImage, mContext);
                            tvMoney.setText("¥ " + BigDecimalUtils.toDecimal(thingsDetails.getPlatform_price(), 2));
                            tvLeaveNumber.setText("库存" + 0 + "件");
                            ((TextView) dialogView.findViewById(R.id.tv_have_choose)).setText("已选择");
                            sum = 0;
                            ToastUtils.show( "该规格没库存了");
                        }
                    }
                    //****************开始匹配********************/
                    for (int i = 0; i < thingsDetails.getSpecTile().size(); i++) {
                        for (int j = 0; j < thingsDetails.getSpecTile().get(i).getValues().size(); j++) {
                            thingsDetails.getSpecTile().get(i).getValues().get(j).setCanSelect(true);
                        }
                    }
                    List<String> idValue = new ArrayList<>();//所选的id在所有可选规格里的可能id
                    List<String> idList = new ArrayList<>();//把这些id拆开，装里面
                    List<String> allIdList = new ArrayList<>();//所有的可能id
                    boolean isMath = true;//是否开始进行匹配
                    //******************如果所有配对都有货就不进行以下所有的匹配了(开始)**********/
                    int totalType = 1;
                    for (int i = 0; i < thingsDetails.getSpecTile().size(); i++) {
                        totalType = totalType * thingsDetails.getSpecTile().get(i).getValues().size();
                    }
                    if (totalType == thingsDetails.getSpecList().size()) {
                        isMath = false;
                    }
                    //******************如果所有配对都有货就不进行以下所有的匹配了(结束)**********/
                    for (int i = 0; i < thingsDetails.getSpecList().size(); i++) {
                        for (Map.Entry<Integer, String> m : map.entrySet()) {
                            if (thingsDetails.getSpecList().get(i).getSpec_value_ids().contains(m.getValue())) {
                                idValue.add(thingsDetails.getSpecList().get(i).getSpec_value_ids());
                            }
                        }
                    }
                    if (map.size() == 0) {//如果现在取消刚在选中的就当前没有一个被选中就不匹配了
                        isMath = false;
                    }
                    if (isMath) {
                        for (int i = 0; i < thingsDetails.getSpecTile().size(); i++) {
                            for (int j = 0; j < thingsDetails.getSpecTile().get(i).getValues().size(); j++) {
                                allIdList.add(thingsDetails.getSpecTile().get(i).getValues().get(j).getId());
                            }
                        }
                        for (int i = 0; i < idValue.size(); i++) {
                            List<String> list = Arrays.asList(idValue.get(i).split(","));
                            for (String s : list) {
                                idList.add(s);
                            }
                        }
                        allIdList.removeAll(idList);
                        for (int n = 0; n < allIdList.size(); n++) {
                            for (int i = 0; i < thingsDetails.getSpecTile().size(); i++) {
                                for (int j = 0; j < thingsDetails.getSpecTile().get(i).getValues().size(); j++) {
                                    if (allIdList.get(n).contains(thingsDetails.getSpecTile().get(i).getValues().get(j).getId())) {
                                        thingsDetails.getSpecTile().get(i).getValues().get(j).setCanSelect(false);
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < thingsDetails.getSpecTile().size(); i++) {
                            for (int j = 0; j < thingsDetails.getSpecTile().get(i).getValues().size(); j++) {
                                Log.v("规格打印", thingsDetails.getSpecTile().get(i).getValues().get(j).getId() + ":" + thingsDetails.getSpecTile().get(i).getValues().get(j).isCanSelect() + "");
                            }
                        }
                    }
                }
                //***********************匹配结束******************************/
                chooseGoodsAdapter.notifyDataSetChanged();
                Log.v("规格", "" + thingsDetails.getSpecTile().get(position).getValues().get(positions).isSelect());
                Log.v("规格", position + "," + map.get(position));
            }
        });
        //****************************选择商品规格（结束）****************************************//
        etGoodNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etGoodNum.setText(s.subSequence(0, 1));
                        etGoodNum.setSelection(1);
                        return;
                    }
                }
                if (s.length() > 0) {
                    if (Integer.parseInt(s.toString().trim()) > sum) {
                        countNum = 1;
                        etGoodNum.setText("1");
                        ToastUtils.show( "不能再加啦!");
                    } else if (Integer.parseInt(s.toString().trim()) < 1) {
                        countNum = 1;
                        etGoodNum.setText("1");
                        ToastUtils.show( "不能再减啦!");
                    } else {
                        countNum = Integer.parseInt(s.toString().trim());
                    }
                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        llClose.setOnClickListener(new View.OnClickListener() {//关闭弹框
            @Override
            public void onClick(View v) {
                for (int i = 0; i < thingsDetails.getSpecTile().size(); i++) {
                    for (int j = 0; j < thingsDetails.getSpecTile().get(i).getValues().size(); j++) {
                        thingsDetails.getSpecTile().get(i).getValues().get(j).setSelect(false);
                        thingsDetails.getSpecTile().get(i).getValues().get(j).setCanSelect(true);
                    }
                }
                mDialog.dismiss();
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countNum < sum) {
                    countNum = countNum + 1;
                    etGoodNum.setText(countNum + "");
                } else {
                    ToastUtils.show("不能再加啦!");
                }
            }
        });
        ivSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countNum > 1) {
                    countNum = countNum - 1;
                    etGoodNum.setText(countNum + "");
                } else {
                    ToastUtils.show("不能再减啦!");
                }
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//确定提交订单
                int id = 0;
                if (map.size() == chooseGoodsAdapter.getItemCount()) {
                    String ids = "";
                    for (int i = 0; i < map.size(); i++) {
                        ids += map.get(i) + ",";
                    }
                    for (int i = 0; i < thingsDetails.getSpecList().size(); i++) {
                        if (ids.contains(thingsDetails.getSpecList().get(i).getSpec_value_ids())) {
                            id = thingsDetails.getSpecList().get(i).getId();
                            sum = thingsDetails.getSpecList().get(i).getNumber();
                        }
                    }
                    Log.v("规格1map.size", map.size() + "，" + ids);
                    Log.v("规格2id", id + "");
                    for (int i = 0; i < thingsDetails.getSpecTile().size(); i++) {
                        for (int j = 0; j < thingsDetails.getSpecTile().get(i).getValues().size(); j++) {
                            thingsDetails.getSpecTile().get(i).getValues().get(j).setSelect(false);
                            thingsDetails.getSpecTile().get(i).getValues().get(j).setCanSelect(true);
                        }
                    }
                    if (id != 0) {
                        if (sum < Integer.parseInt(etGoodNum.getText().toString().trim())) {
                            etGoodNum.setText("1");
                        }
                        mDialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("whichActivity", "GroupBuyingActivity");
                        bundle.putInt("good_id", thingsDetails.getGoods_id());
                        bundle.putInt("good_id_ggs", id);
                        bundle.putInt("count", Integer.parseInt(etGoodNum.getText().toString().trim()));
                        bundle.putInt("type", 1);
//                        RxActivityTool.skipActivity(GroupBuyingActivity.this, SureOrderActivity.class, bundle);
                    } else {
                        ToastUtils.show("请重新选择规格!");
                    }
                } else {
                    Log.v("规格", "请选择商品规格");
                    ToastUtils.show( "请选择商品规格!");
                }
            }
        });
    }

    private void initSlideDetailsLayout() {
        mSlideDetailsLayout.setScrollStatusListener(new SlideAnimLayout.onScrollStatusListener() {
            @Override
            public void onStatusChanged(SlideAnimLayout.Status mNowStatus, boolean isHalf) {
                if(mNowStatus== SlideAnimLayout.Status.CLOSE){
                    //打开
                    if(isHalf){
                        mTvMoreText.setText("释放，查看图文详情");
                        mIvMoreImg.animate().rotation(0);
                        LoggerUtils.i("onStatusChanged---CLOSE---释放"+isHalf);
                    }else{//关闭
                        mTvMoreText.setText("继续上拉，查看图文详情");
                        mIvMoreImg.animate().rotation(180);
                        LoggerUtils.i("onStatusChanged---CLOSE---继续上拉"+isHalf);
                    }
                }else{
                    //打开
                    if(isHalf){
                        mTvMoreText.setText("下拉回到商品详情");
                        mIvMoreImg.animate().rotation(0);
                        LoggerUtils.i("onStatusChanged---OPEN---下拉回到商品详情"+isHalf);
                    }else{//关闭
                        mTvMoreText.setText("释放回到商品详情");
                        mIvMoreImg.animate().rotation(180);
                        LoggerUtils.i("onStatusChanged---OPEN---释放回到商品详情"+isHalf);
                    }
                }
            }
        });
        mSlideDetailsLayout.setOnSlideStatusListener(new SlideAnimLayout.OnSlideStatusListener() {
            @Override
            public void onStatusChanged(SlideAnimLayout.Status status) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)
                        root.getLayoutParams();
                if (status == SlideAnimLayout.Status.OPEN) {
                    layoutParams.topMargin = dip2px(ShopDetialActivity.this,44.0f);
                    root.setLayoutParams(layoutParams);
                    LoggerUtils.i("setOnSlideStatusListener---OPEN---下拉回到商品详情");
                    //scrollView.scrollTo(0,0);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            //ScrollView滑动到顶部
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                } else {
                    layoutParams.topMargin = dip2px(ShopDetialActivity.this,0);
                    root.setLayoutParams(layoutParams);
                    LoggerUtils.i("setOnSlideStatusListener---CLOSE---上拉查看图文详情");
                }
            }
        });
    }


    @SuppressLint({"ObsoleteSdkInt", "SetJavaScriptEnabled"})
    private void initWebView() {
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
            new Object() {
                void setLoadWithOverviewMode(boolean overview) {
                    settings.setLoadWithOverviewMode(overview);
                }
            }.setLoadWithOverviewMode(true);
        }

        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("https://item.jd.com/100016046828.html#crumb-wrap");
            }
        });
    }




    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //本地数据测试专用
    private void initJsonData() {
        try {
            InputStream is = getAssets().open("goods.json");//打开json数据
            byte[] by = new byte[is.available()];//转字节
            is.read(by);
            jsonString = new String(by, "utf-8");
            is.close();//关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //解析Json数据
    private void getData() {
        Gson gson = new Gson();
        thingsDetails = gson.fromJson(jsonString, ThingsDetails.class);
    }

    @Override
    public void sendMessage(String msg) {
        showShopDialog();
    }
}