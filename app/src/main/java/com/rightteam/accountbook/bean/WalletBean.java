package com.rightteam.accountbook.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by JasonWu on 7/21/2018
 */
@Entity
public class WalletBean {
    @Id(autoincrement = true)
    private Long id;
    private int imageId;
    private String title;
    private long startTime;
    @Generated(hash = 499133319)
    public WalletBean(Long id, int imageId, String title, long startTime) {
        this.id = id;
        this.imageId = imageId;
        this.title = title;
        this.startTime = startTime;
    }
    @Generated(hash = 1814219826)
    public WalletBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getImageId() {
        return this.imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public long getStartTime() {
        return this.startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
