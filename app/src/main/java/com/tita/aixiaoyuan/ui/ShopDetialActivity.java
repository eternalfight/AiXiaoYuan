package com.tita.aixiaoyuan.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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

import com.bumptech.glide.Glide;
import com.hjq.toast.ToastUtils;
import com.tita.aixiaoyuan.Chat.activity.ChatActivity;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.fragment.ShopMainFragment;
import com.tita.aixiaoyuan.app.widget.CustomLinearLayoutManager;
import com.tita.aixiaoyuan.model.OrderCarBean;
import com.tita.aixiaoyuan.model.User;
import com.tita.aixiaoyuan.model.productInfoBean;
import com.wang.avi.AVLoadingIndicatorView;
import com.ycbjie.slide.LoggerUtils;
import com.ycbjie.slide.SlideAnimLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Thread.sleep;

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
    @BindView(R.id.btn_kefu) TextView Btn_kefu;
    @BindView(R.id.sale_publish) TextView sale_publish;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    private int countNum = 1;//购买商品数量
    TagFlowLayout tagFlowLayout;
    BmobQuery<productInfoBean> bmobQuery;
    private String[] goodsName = new String[]{""};
    private int goods_choose_flag = 0;//商品tag点击标记
    private int buyNow = 0;  //0：加入购物车；1：立即购买
    private String goodsObjId;
    private int isPreViewMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detial);
        ButterKnife.bind(this);
        startAnim();
        preViewMode();
        getData();
        initView();
        initListener();
        initShopMainFragment();
        initSlideDetailsLayout();
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
    // 返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 如果是手机上的返回键
            finish();
        }
        return super.onKeyDown(keyCode, event);
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

    @OnClick({R.id.back_btn,R.id.buyNow,R.id.addtocontent,R.id.btn_kefu,R.id.sale_publish})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                if (isPreViewMode == 1){
                    // TODO: 2021/2/26 删除上传数据
                    deleteUploadData();
                }
                finish();
                break;
            case R.id.buyNow:
                if (isPreViewMode == 1){
                    ToastUtils.show("当前是预览模式，不支持该操作！");
                }else {
                    buyNow = 1;
                    showShopDialog();
                }

                break;
            case R.id.addtocontent:
                if (isPreViewMode == 1){
                    ToastUtils.show("当前是预览模式，不支持该操作！");
                }else {
                    buyNow = 0;
                    showShopDialog();
                }

                break;
            case R.id.ll_current_goods:

                break;
            case R.id.btn_kefu:
                if (isPreViewMode == 1){
                    ToastUtils.show("当前是预览模式，不支持该操作！");
                }else {
                    Intent intent = new Intent(this, ChatActivity.class);
                    startActivity(intent);
                }

            case R.id.sale_publish:
                if ( isPreViewMode == 1 ){
                    updateData();
                    SweetAlertDialog mDialog = new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
                    mDialog.setTitleText("发布成功!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                   // Intent intent1 = new Intent(this,PublishSuccessActivity.class);
                                   // startActivity(intent1);
                                    saleActivity.instance.finish();
                                    finish();
                                }
                            }).show();

                }
                Log.i("flag", "show: " + flag);
                break;
        }
    }

    private void showShopDialog() {
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
        TextView tv_have_choose = dialogView.findViewById(R.id.tv_have_choose);
        final ImageView ivImage = dialogView.findViewById(R.id.iv_image);
        final TextView tvMoney = dialogView.findViewById(R.id.tv_money);
        final TextView tvLeaveNumber = dialogView.findViewById(R.id.tv_leave_number);
        LinearLayout llClose = dialogView.findViewById(R.id.ll_close);
        ImageView ivSub = dialogView.findViewById(R.id.iv_sub);
        final EditText etGoodNum = dialogView.findViewById(R.id.et_good_num);
        ImageView ivAdd = dialogView.findViewById(R.id.iv_add);
        final Button btnSure = dialogView.findViewById(R.id.btn_sure);


        //tvMoney.setText("¥ " + BigDecimalUtils.toDecimal(price, 2));
        //tvLeaveNumber.setText("库存" + current_cnt + "件");

         //****************************选择商品规格（开始）****************************************//
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(false);
        final LayoutInflater mInflater = LayoutInflater.from(ShopDetialActivity.this);
        tagFlowLayout = (TagFlowLayout) dialogView.findViewById(R.id.id_flowlayout);

        goodsName[0] = product_name;
        tvMoney.setText("¥ " + price.toString());
        tvLeaveNumber.setText("库存" + current_cnt + "件");
        try {
            Glide.with(this)
                    .load(PicUrl.get(0))
                    .into(ivImage);
        }catch (Exception e){
            Log.e("TAG", "showShopDialog: " + e.getMessage() );
        }


        tagFlowLayout.setAdapter(new TagAdapter<String>(goodsName)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_goods_tv, tagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {

                return true;
            }
        });


        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
                if (selectPosSet.size()>0){
                    tv_have_choose.setText("已选择："+ goodsName[0]);
                    goods_choose_flag = 1;
                }else {
                    tv_have_choose.setText("");
                    goods_choose_flag = 0;
                }

            }
        });
        //数量输入监听
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
                    if (Integer.parseInt(s.toString().trim()) > current_cnt) {
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
            mDialog.dismiss();
        }
    });
        ivAdd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (goods_choose_flag == 0){
                ToastUtils.show("请先选择商品");
            }else {
                if (countNum < current_cnt) {
                    countNum = countNum + 1;
                    etGoodNum.setText(countNum + "");
                } else {
                    ToastUtils.show("不能再加啦!");
                }
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
            // TODO: 2021/2/26  确定提交订单
            if (goods_choose_flag == 0){
                ToastUtils.show("请先选择商品");
            }else {
                if (countNum <= current_cnt){
                    if (buyNow == 0){
                        addtoCar();
                        mDialog.dismiss();
                    }else {

                    }
                }else {
                    ToastUtils.show("没有货啦!");
                }
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
    private void initWebView(String product_detial_pic) {
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
                webView.loadUrl(product_detial_pic);
            }
        });
    }




    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    //获取数据
    private void getData() {
        query();
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Exception {
                for (int i=1;;i++){
                    sleep(1000);
                    emitter.onNext(i);
                    if (product_name != ""){
                        break;
                    }
                }

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        //JUtils.Toast(product_name);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除所有视图
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        getWindowManager().removeView(view);
        System.gc();
    }

    //预览模式
    private void preViewMode(){

        Intent intent = getIntent();
        goodsObjId = intent.getStringExtra("GoodsObjectId");
        isPreViewMode = intent.getIntExtra("isPreView",0);
        if (isPreViewMode == 0){
            //JUtils.Toast("非预览模式");
        }else {
            //JUtils.Toast("预览模式");
            sale_publish.setVisibility(View.VISIBLE);
        }

    }

    productInfoBean productInfoBean;
    private String product_id;
    private String product_name = "";    //商品名称
    private String username;        //发布的用户
    private int one_category_id;    //商品类型
    private String product_info;    //商品介绍
    private Double price;           //价格
    private int current_cnt;        //库存
    private int publish_status;     //商品发布状态
    private String product_detial_pic; //商品详情图
    private BmobFile picOne;         //商品主图
    private BmobFile picTwo;         //第二章张图
    private BmobFile picThr;
    private BmobFile picFor;
    private BmobFile picFiv;
    private BmobFile picSix;
    private List<String> PicUrl;
    Observable observable;
    private int flag = 0;
    private String objID = "";
    /**
     *
     * 查询一个对象
     */
    private void query() {
        bmobQuery = new BmobQuery<>();
        productInfoBean = new productInfoBean();

        //Log.i("bmobQuery", "query  goodsObjId: " + goodsObjId);
        if (isPreViewMode == 1){
            objID = goodsObjId;
        }else {
            objID = goodsObjId;
        }
        //Log.i("bmobQuery", "query objID: " + objID);
        bmobQuery.getObject(objID, new QueryListener<productInfoBean>() {
            @Override
            public void done(productInfoBean product, BmobException e) {
                if (e == null) {
                    product_id = product.getProduct_id();
                    product_name = product.getProduct_name();
                    username = product.getUsername();
                    product_info = product.getProduct_info();
                    price = product.getPrice();
                    current_cnt = product.getCurrent_cnt();
                    try {
                        product_detial_pic = product.getProduct_detial_pic().getFileUrl();
                    }catch (Exception e1){

                    }
                    PicUrl = product.getPicUrl();
                    initWebView(product_detial_pic);
                    Log.i("BMOB",  product_detial_pic);
                    stopAnim();
                    flag = 1;
                    Log.i("flag", "done: " + flag);
                } else {
                    Log.e("BMOB", e.toString());
                    stopAnim();

                }
            }

        });
    }
    /**
     * 删除一个对象
     */
    private void deleteUploadData() {
        //productInfoBean = new productInfoBean();
        productInfoBean.delete(goodsObjId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.show("删除成功");
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
    private void updateData(){
        productInfoBean = new productInfoBean();
        productInfoBean.setPublish_status(1);
        Log.i("update", "onViewClicked: " + objID);
        productInfoBean.update(objID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //ToastUtils.show("update：" + e.getMessage());
                    Log.i("updateData", "update success");

                } else {
                    //ToastUtils.show("update：" + e.getMessage());
                    Log.i("updateData", "update false: " + e.getMessage());

                }
            }
        });
    }

    private void addtoCar(){
        BmobUser user = BmobUser.getCurrentUser(User.class);
        String username = user.getUsername();
        String bql ="select * from OrderCarBean where username ='"+username+"' and product_objectId = '" + objID +"'";
        BmobQuery<OrderCarBean> query = new BmobQuery<OrderCarBean>();
        query.setSQL(bql);
        query.order("-updatedAt");
        query.doSQLQuery(new SQLQueryListener<OrderCarBean>() {
            @Override
            public void done(BmobQueryResult<OrderCarBean> result, BmobException e) {
                avi.hide();
                if (e == null){
                    //Log.i("doSearch", "doSearch: "+ result.getCount());
                    List<OrderCarBean> list = (List<OrderCarBean>) result.getResults();
                    if(list != null && list.size()>0){
                        ToastUtils.show("该商品已经在购物车中");
                    }else {
                        try {
                            OrderCarBean orderCarBean = new OrderCarBean();
                            orderCarBean.setUsername(user.getUsername());
                            orderCarBean.setProduct_amount(countNum);
                            orderCarBean.setProduct_id(product_id);
                            orderCarBean.setProduct_objectId(objID);
                            orderCarBean.setProduct_name(product_name);
                            orderCarBean.setProduct_Price(price.toString());
                            orderCarBean.setProduct_picUrl(PicUrl.get(0));

                            orderCarBean.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        ToastUtils.show("成功添加购物车");
                                    } else {
                                        ToastUtils.show("添加失败："+e.getMessage());
                                        Log.e("BMOB", e.toString());
                                    }
                                }
                            });

                        }catch (Exception e1){
                            Log.e("TAG", "done: "+ e.getMessage() );
                        }

                    }

                }else{
                    Log.i("TAG", "done: " + e.getMessage());
                }
            }
        });




    }


    @Override
    public void sendMessage(String msg) {
        while (flag == 1){
            showShopDialog();
            break;
        }

    }
    void startAnim(){
       // avi.show();
        avi.smoothToShow();
    }

    void stopAnim(){
        //avi.hide();
        avi.smoothToHide();
    }

}