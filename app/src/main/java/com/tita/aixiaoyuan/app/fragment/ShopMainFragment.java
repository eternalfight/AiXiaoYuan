package com.tita.aixiaoyuan.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.productInfoBean;
import com.tita.aixiaoyuan.utils.Utils;
import com.yc.cn.ycbannerlib.banner.BannerView;
import com.yc.cn.ycbannerlib.banner.adapter.AbsStaticPagerAdapter;
import com.yc.cn.ycbannerlib.banner.hintview.TextHintView;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


public class ShopMainFragment extends BaseFragmentJava {

    private ScrollView mScrollView;
    private TextView mTvGoodsTitle;
    private TextView mTvNewPrice;
    private TextView mTvOldPrice;
    private LinearLayout mLlActivity;
    private LinearLayout mLlCurrentGoods;
    private TextView mTvCurrentGoods;
    private ImageView mIvEnsure;
    private LinearLayout mLlComment;
    private TextView mTvCommentCount;
    private TextView mTvGoodComment;
    private ImageView mIvCommentRight;
    private LinearLayout mLlEmptyComment;
    private LinearLayout mLlRecommend;
    private TextView mTvBottomView;
    private SendMessageCommunitor sendMessage;
    @BindView(R.id.good_bannerView)
    BannerView banner;
    private List<String> PicUrl;
    private Double NewPrice;
    private String goodsTitle;

    private String goodsObjId;
    private int isPreViewMode = 0;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.include_shop_main;
    }

    @Override
    protected void setUpView() {
        initView();
    }

    @Override
    protected void setUpData() {

    }



    private void initView() {
        preViewMode();
        query();
        View view = getContentView();
        mScrollView = view.findViewById(R.id.scrollView);
        mTvGoodsTitle = view.findViewById(R.id.tv_goods_title);
        mTvNewPrice = view.findViewById(R.id.tv_new_price);
        mTvOldPrice = view.findViewById(R.id.tv_old_price);
        mLlCurrentGoods = view.findViewById(R.id.ll_current_goods);
        mTvCurrentGoods = view.findViewById(R.id.tv_current_goods);
        //mIvEnsure = view.findViewById(R.id.iv_ensure);
        mLlComment = view.findViewById(R.id.ll_comment);
        mTvCommentCount = view.findViewById(R.id.tv_comment_count);
        mTvGoodComment = view.findViewById(R.id.tv_good_comment);
        mIvCommentRight = view.findViewById(R.id.iv_comment_right);
        mLlEmptyComment = view.findViewById(R.id.ll_empty_comment);
        mLlRecommend = view.findViewById(R.id.ll_recommend);
        mTvBottomView = view.findViewById(R.id.tv_bottom_view);

        mLlCurrentGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage.sendMessage("open");
            }
        });


    }


    public void changBottomView(boolean isDetail) {
        if(isDetail){
            mTvBottomView.setText("下拉回到商品详情");
        }else {
            mTvBottomView.setText("继续上拉，查看图文详情");
        }
    }


    /**
     *
     * 用于fragment传递事件给activity
     */
    public interface SendMessageCommunitor {
        /**从fragment发送消息
         * @param msg 消息内容
         * */
        void sendMessage(String msg);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sendMessage = (SendMessageCommunitor) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(banner!=null){
            banner.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(banner!=null){
            banner.pause();
        }
    }

    private void initBanner() {

        banner.setAnimationDuration(1000);
        banner.setHintPadding(0, Utils.dip2px(getMContext(),10f),
                Utils.dip2px(getMContext(),10f),Utils.dip2px(getMContext(),10f));
        banner.setPlayDelay(2000);
        banner.setHintView(new TextHintView(getMContext()));
        ImageNormalAdapter imageNormalAdapter = new ImageNormalAdapter();
        banner.setAdapter(imageNormalAdapter);
        banner.setOnBannerClickListener(new BannerView.OnBannerClickListener() {
            @Override
            public void onItemClick(int position) {
                //ToastUtils.show(position+"被点击呢");
            }
        });
    }
    private void query() {
        BmobQuery bmobQuery = new BmobQuery<>();
        String objID = "";
        Log.i("bmobQuery", "query  goodsObjId: " + goodsObjId);
        if (isPreViewMode == 1){
            objID = goodsObjId;
        }else {
            objID = goodsObjId;
        }
        Log.i("bmobQuery", "query objID: " + objID);

        bmobQuery.getObject(objID, new QueryListener<productInfoBean>() {
            @Override
            public void done(productInfoBean product, BmobException e) {
                if (e == null) {
                    PicUrl = product.getPicUrl();
                    NewPrice = product.getPrice();
                    goodsTitle = product.getProduct_name();

                    mTvNewPrice.setText(NewPrice.toString());
                    mTvOldPrice.setText("");
                    mTvGoodsTitle.setText(goodsTitle);

                    for (String s: PicUrl){
                        Log.i("BMOB", s);
                    }
                    initBanner();
                } else {
                    Log.e("BMOB", e.toString());

                }
            }
        });
    }

    private class ImageNormalAdapter extends AbsStaticPagerAdapter {


        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
            try {
                Glide.with(getActivity())
                        .load(PicUrl.get(position))
                        .into(view);
            }catch (Exception e){
                Log.e("getView", "getView: " + e.getMessage());
            }
            return view;
        }
        @Override
        public int getCount() {
            return PicUrl.size();
        }
    }
    //预览模式
    private void preViewMode(){

        Intent intent = getActivity().getIntent();
        goodsObjId = intent.getStringExtra("GoodsObjectId");
        isPreViewMode = intent.getIntExtra("isPreView",0);
        if (isPreViewMode == 0){
            //JUtils.Toast("非预览模式");
        }else {
            //JUtils.Toast("预览模式");
        }

    }
}
