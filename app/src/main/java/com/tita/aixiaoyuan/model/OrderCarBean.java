package com.tita.aixiaoyuan.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class OrderCarBean extends BmobObject implements Serializable {

    private String username;  //用户名
    private int product_amount;  //商品数量
    private String product_id;   //商品id
    private String product_objectId;  //商品objectId
    private String product_name;
    private String product_Price;
    private String product_picUrl = "";



    public OrderCarBean() {

    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_Price() {
        return product_Price;
    }

    public void setProduct_Price(String product_Price) {
        this.product_Price = product_Price;
    }

    public String getProduct_picUrl() {
        return product_picUrl;
    }

    public void setProduct_picUrl(String product_picUrl) {
        this.product_picUrl = product_picUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getProduct_amount() {
        return product_amount;
    }

    public void setProduct_amount(int product_amount) {
        this.product_amount = product_amount;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_objectId() {
        return product_objectId;
    }

    public void setProduct_objectId(String product_objectId) {
        this.product_objectId = product_objectId;
    }



    public static class ItemsBean {
        private boolean ischeck=false;

        public boolean isIscheck() {
            return ischeck;
        }

        public void setIscheck(boolean ischeck) {
            this.ischeck = ischeck;
        }
    }

}
