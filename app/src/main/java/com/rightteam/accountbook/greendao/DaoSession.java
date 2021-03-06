package com.rightteam.accountbook.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.bean.WalletBean;

import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.greendao.WalletBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig billBeanDaoConfig;
    private final DaoConfig walletBeanDaoConfig;

    private final BillBeanDao billBeanDao;
    private final WalletBeanDao walletBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        billBeanDaoConfig = daoConfigMap.get(BillBeanDao.class).clone();
        billBeanDaoConfig.initIdentityScope(type);

        walletBeanDaoConfig = daoConfigMap.get(WalletBeanDao.class).clone();
        walletBeanDaoConfig.initIdentityScope(type);

        billBeanDao = new BillBeanDao(billBeanDaoConfig, this);
        walletBeanDao = new WalletBeanDao(walletBeanDaoConfig, this);

        registerDao(BillBean.class, billBeanDao);
        registerDao(WalletBean.class, walletBeanDao);
    }
    
    public void clear() {
        billBeanDaoConfig.clearIdentityScope();
        walletBeanDaoConfig.clearIdentityScope();
    }

    public BillBeanDao getBillBeanDao() {
        return billBeanDao;
    }

    public WalletBeanDao getWalletBeanDao() {
        return walletBeanDao;
    }

}
