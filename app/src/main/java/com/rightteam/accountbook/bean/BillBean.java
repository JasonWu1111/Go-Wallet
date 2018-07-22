package com.rightteam.accountbook.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by JasonWu on 7/21/2018
 */
@Entity
public class BillBean {
    @Id(autoincrement = true)
    private Long id;
    private float price;
    private long time;
    private int category;
    private int type;
    private Long walletId;
    private String remark;
    @Generated(hash = 901322169)
    public BillBean(Long id, float price, long time, int category, int type,
            Long walletId, String remark) {
        this.id = id;
        this.price = price;
        this.time = time;
        this.category = category;
        this.type = type;
        this.walletId = walletId;
        this.remark = remark;
    }
    @Generated(hash = 562884989)
    public BillBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public float getPrice() {
        return this.price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public int getCategory() {
        return this.category;
    }
    public void setCategory(int category) {
        this.category = category;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Long getWalletId() {
        return this.walletId;
    }
    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
