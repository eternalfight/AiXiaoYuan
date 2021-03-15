package com.tita.aixiaoyuan.model;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;


public class MyOrderBean extends BmobObject implements Serializable {
   private String username;
   private String orderId;
   private List<String> product_price ;
   private int order_status;
   private List<String> product_id;
   private List<String> product_name;
   private List<Integer> product_cnt;
   private String receive_time;
   private String price;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<String> getProduct_price() {
        return product_price;
    }

    public void setProduct_price(List<String> product_price) {
        this.product_price = product_price;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public List<String> getProduct_id() {
        return product_id;
    }

    public void setProduct_id(List<String> product_id) {
        this.product_id = product_id;
    }

    public List<String> getProduct_name() {
        return product_name;
    }

    public void setProduct_name(List<String> product_name) {
        this.product_name = product_name;
    }

    public List<Integer> getProduct_cnt() {
        return product_cnt;
    }

    public void setProduct_cnt(List<Integer> product_cnt) {
        this.product_cnt = product_cnt;
    }

    public String getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(String receive_time) {
        this.receive_time = receive_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
