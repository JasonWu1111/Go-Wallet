package com.rightteam.accountbook.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by JasonWu on 7/21/2018
 */
@Entity
public class BillBean {
    @Id
    private int id;
    private float price;
    @Generated(hash = 509846597)
    public BillBean(int id, float price) {
        this.id = id;
        this.price = price;
    }
    @Generated(hash = 562884989)
    public BillBean() {
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public float getPrice() {
        return this.price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
}
