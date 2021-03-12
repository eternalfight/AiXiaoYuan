package com.tita.aixiaoyuan.model;

import java.util.List;

public class OrderBean {
    private String msg;
    private String code;
    private String page;
    private List<DataBean> data;

    public OrderBean(String msg,String code,String page,List<DataBean> data){
        this.msg = msg;
        this.code = code;
        this.page = page;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String createtime;  //创建时间
        private int orderid;        //订单号
        private double price;       //价格
        private int status;         //订单状态
        private String title;       //商品名
        private int uid;            //商品id

        public DataBean(){

        }
        public DataBean(String createtime, int orderid, double price, int status, String title, int uid){
            this.createtime = createtime;
            this.orderid = orderid;
            this.price = price;
            this.status  = status;
            this.title = title;
            this.uid = uid;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
