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
    private String category;
    private int type;
    private Long walletId;
    private String memo;
    private boolean isExpense;
    @Generated(hash = 1134697105)
    public BillBean(Long id, float price, long time, String category, int type,
            Long walletId, String memo, boolean isExpense) {
        this.id = id;
        this.price = price;
        this.time = time;
        this.category = category;
        this.type = type;
        this.walletId = walletId;
        this.memo = memo;
        this.isExpense = isExpense;
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
    public String getCategory() {
        return this.category;
    }
    public void setCategory(String category) {
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
    public String getMemo() {
        return this.memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public boolean getIsExpense() {
        return this.isExpense;
    }
    public void setIsExpense(boolean isExpense) {
        this.isExpense = isExpense;
    }

}
