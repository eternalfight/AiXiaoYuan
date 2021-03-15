package com.tita.aixiaoyuan.Adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.EventMsg;
import com.tita.aixiaoyuan.model.productInfoBean;
import com.tita.aixiaoyuan.utils.RxBus;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GoodsSearchResultAdapter extends BaseQuickAdapter<productInfoBean, BaseViewHolder> {
    private Context mContext;
    public static final int SEALER = 1;// 卖家
    public static final int BUYER = 0;// 加载更多
    private int type = 0;
    private volatile int flag = 0;
    public GoodsSearchResultAdapter(Context mContext, @Nullable List<productInfoBean> goodsData, int type) {
        super(R.layout.search_item, goodsData);
        this.mContext = mContext;
        this.type = type;
    }
    public GoodsSearchResultAdapter(Context mContext, @Nullable List<productInfoBean> goodsData) {
        super(R.layout.search_item, goodsData);
        this.mContext = mContext;
    }
    @Override
    protected void convert(BaseViewHolder holder, productInfoBean item) {
        getAdapterMsg();
        try{

            if (type == 1){
                holder.setVisible(R.id.count,true);
                holder.setText(R.id.count,"库存：" + item.getCurrent_cnt());
            }
            if (item.getPublish_status()==2 && flag==0){
                holder.setVisible(R.id.goto_deliver,true);
                holder.addOnClickListener(R.id.goto_deliver);
            }else {
                holder.setVisible(R.id.goto_deliver,false);
            }
            holder.setText(R.id.good_name_tv,item.getProduct_name());
            holder.setText(R.id.goods_price,item.getPrice().toString());
            Glide.with(mContext).load(item.getPicOne().getUrl()).into((ImageView) holder.getView(R.id.goods_img));
        }catch (Exception e){
            Log.e(TAG, "convert: " + e.getMessage());
        }
    }
    private void getAdapterMsg(){
        Disposable subscribe = RxBus.getInstance().toObservable().map(new Function<Object, EventMsg>() {
            @Override
            public EventMsg apply(Object o) throws Exception {
                return (EventMsg) o;
            }
        }).subscribe(new Consumer<EventMsg>() {
            @Override
            public void accept(EventMsg eventMsg) throws Exception {
                if (eventMsg != null) {
                    if (eventMsg.getMsg().equals("MySellFragment")){
                        flag=1;
                    }
                }
            }
        });
    }
}
