package com.tita.aixiaoyuan.app.fragment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.utils.Utils;
import com.yc.cn.ycbannerlib.banner.BannerView;
import com.yc.cn.ycbannerlib.banner.adapter.AbsStaticPagerAdapter;
import com.yc.cn.ycbannerlib.banner.hintview.TextHintView;

import butterknife.BindView;


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
    private int[] imgs;


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
        View view = getContentView();
        imgs = new int[]{R.drawable.advertise0,R.drawable.advertise1,R.drawable.advertise2,R.drawable.advertise3,R.drawable.advertise4};
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
        initBanner();
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
        banner.setAdapter(new ImageNormalAdapter());
        banner.setOnBannerClickListener(new BannerView.OnBannerClickListener() {
            @Override
            public void onItemClick(int position) {
                ToastUtils.show(position+"被点击呢");
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
            view.setImageResource(imgs[position]);
            return view;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }
    }
}
