package com.rightteam.accountbook.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.rightteam.accountbook.greendao.DaoSession;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.greendao.WalletBeanDao;

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

    @ToMany(referencedJoinProperty = "walletId")
    private List<BillBean> bills;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2088146005)
    private transient WalletBeanDao myDao;

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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 370188635)
    public List<BillBean> getBills() {
        if (bills == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BillBeanDao targetDao = daoSession.getBillBeanDao();
            List<BillBean> billsNew = targetDao._queryWalletBean_Bills(id);
            synchronized (this) {
                if (bills == null) {
                    bills = billsNew;
                }
            }
        }
        return bills;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2044382492)
    public synchronized void resetBills() {
        bills = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1000844297)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWalletBeanDao() : null;
    }
}
