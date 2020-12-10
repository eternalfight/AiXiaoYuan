package com.tita.aixiaoyuan.model;


import butterknife.Unbinder;

import java.io.Serializable;

public class ButterKnifeSerializable implements Serializable {

    private Unbinder unbinder;
    public void setUnbinder(Unbinder unbinder){
        this.unbinder = unbinder;
    }
    public Unbinder getUnbinder(){
        return unbinder;
    }
}