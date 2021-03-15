package com.tita.aixiaoyuan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class productInfoBean extends BmobObject implements Parcelable {

    private String product_id;
    private String product_name;    //商品名称
    private String username;        //发布的用户
    private int one_category_id;    //商品类型
    private String product_info;
    private Double price;           //价格
    private int current_cnt = 1;        //库存
    private int publish_status;     //商品发布状态
    private BmobFile product_detial_pic; //商品详情图
    private BmobFile picOne;         //商品主图
    private BmobFile picTwo;         //第二章张图
    private BmobFile picThr;
    private BmobFile picFor;
    private BmobFile picFiv;
    private BmobFile picSix;
    private List<String> PicUrl;
    private int sellout = 0;    //商品介绍

    public productInfoBean(Parcel in) {
        product_id = in.readString();
        product_name = in.readString();
        username = in.readString();
        one_category_id = in.readInt();
        product_info = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        current_cnt = in.readInt();
        publish_status = in.readInt();
        PicUrl = in.createStringArrayList();
        sellout = in.readInt();
    }

    public static final Creator<productInfoBean> CREATOR = new Creator<productInfoBean>() {
        @Override
        public productInfoBean createFromParcel(Parcel in) {
            return new productInfoBean(in);
        }

        @Override
        public productInfoBean[] newArray(int size) {
            return new productInfoBean[size];
        }
    };

    public productInfoBean() {

    }

    public int getSellout() {
        return sellout;
    }

    public void setSellout(int sellout) {
        this.sellout = sellout;
    }

    public List<String> getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(List<String> picUrl) {
        PicUrl = picUrl;
    }

    public BmobFile getPicOne() {
        return picOne;
    }

    public void setPicOne(BmobFile picOne) {
        this.picOne = picOne;
    }

    public BmobFile getPicTwo() {
        return picTwo;
    }

    public void setPicTwo(BmobFile picTwo) {
        this.picTwo = picTwo;
    }

    public BmobFile getPicThr() {
        return picThr;
    }

    public void setPicThr(BmobFile picThr) {
        this.picThr = picThr;
    }

    public BmobFile getPicFor() {
        return picFor;
    }

    public void setPicFor(BmobFile picFor) {
        this.picFor = picFor;
    }

    public BmobFile getPicFiv() {
        return picFiv;
    }

    public void setPicFiv(BmobFile picFiv) {
        this.picFiv = picFiv;
    }

    public BmobFile getPicSix() {
        return picSix;
    }

    public void setPicSix(BmobFile picSix) {
        this.picSix = picSix;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getOne_category_id() {
        return one_category_id;
    }

    public void setOne_category_id(int one_category_id) {
        this.one_category_id = one_category_id;
    }

    public String getProduct_info() {
        return product_info;
    }

    public void setProduct_info(String product_info) {
        this.product_info = product_info;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getCurrent_cnt() {
        return current_cnt;
    }

    public void setCurrent_cnt(int current_cnt) {
        this.current_cnt = current_cnt;
    }

    public int getPublish_status() {
        return publish_status;
    }

    public void setPublish_status(int publish_status) {
        this.publish_status = publish_status;
    }

    public BmobFile getProduct_detial_pic() {
        return product_detial_pic;
    }

    public void setProduct_detial_pic(BmobFile product_detial_pic) {
        this.product_detial_pic = product_detial_pic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(product_id);
        dest.writeString(product_name);
        dest.writeString(username);
        dest.writeInt(one_category_id);
        dest.writeString(product_info);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        dest.writeInt(current_cnt);
        dest.writeInt(publish_status);
        dest.writeStringList(PicUrl);
        dest.writeInt(sellout);
    }
}
