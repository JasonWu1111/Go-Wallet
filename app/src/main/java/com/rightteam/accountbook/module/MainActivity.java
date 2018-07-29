package com.rightteam.accountbook.module;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.MainAdapter;
import com.rightteam.accountbook.adapter.OnItemClickListener;
import com.rightteam.accountbook.adapter.WalletListAdapter;
import com.rightteam.accountbook.base.BaseActivity;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.bean.WalletBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.event.UpdateBillListEvent;
import com.rightteam.accountbook.event.UpdateWalletListEvent;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.greendao.WalletBeanDao;
import com.rightteam.accountbook.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.drawer_recycler_view)
    RecyclerView walletList;
    @BindView(R.id.drawer_view)
    LinearLayout drawerView;

    private long mCurWalletId;
    private WalletListAdapter mWalletAdapter;
    private WalletBeanDao walletBeanDao = MyApplication.getsDaoSession().getWalletBeanDao();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        mCurWalletId = SharedPreferencesUtil.getInstance().getLong(KeyDef.CURRENT_WALLET_ID, -1);
        if(mCurWalletId == -1){
            WalletBean bean = new WalletBean(null, 0, "Wallet1", System.currentTimeMillis());
            walletBeanDao.insert(bean);
            WalletBean bean1 = walletBeanDao.queryBuilder().list().get(0);
            mCurWalletId = bean1.getId();
            SharedPreferencesUtil.getInstance().putLong(KeyDef.CURRENT_WALLET_ID, bean1.getId());
        }

        List<String> titles = new ArrayList<>();
        titles.add("Transaction");
        titles.add("Statements");
        List<Fragment> fragments = new ArrayList<>();
        Fragment fragment = new TransactionFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KeyDef.WALLET_ID, mCurWalletId);
        fragment.setArguments(bundle);
        fragments.add(fragment);
        fragments.add(new Fragment());
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(mainAdapter);
        tabLayout.setViewPager(viewPager);

        initDrawerView();

        mWalletAdapter = new WalletListAdapter(this);
        mWalletAdapter.setOnItemClickListener((position, action, id) -> {
            switch (action){
                case KeyDef.ACTION_DELETE:
                    if(walletBeanDao.queryBuilder().list().size() > 1){
                        walletBeanDao.deleteByKey(id);
                        mCurWalletId = walletBeanDao.queryBuilder().list().get(0).getId();
                        updateData();
                        EventBus.getDefault().post(new UpdateBillListEvent(mCurWalletId));
                        SharedPreferencesUtil.getInstance().putLong(KeyDef.CURRENT_WALLET_ID, mCurWalletId);
                        BillBeanDao billBeanDao = MyApplication.getsDaoSession().getBillBeanDao();
                        List<BillBean> billBeans = billBeanDao.queryBuilder().where(BillBeanDao.Properties.WalletId.eq(id)).list();
                        billBeanDao.deleteInTx(billBeans);
                    }
                    break;
                case KeyDef.ACTION_MODIFY:
                    startActivity(new Intent(MainActivity.this, WalletActivity.class).putExtra(KeyDef.WALLET_ID, id));
                    break;
            }
        });
        walletList.setLayoutManager(new LinearLayoutManager(this));
        walletList.setAdapter(mWalletAdapter);
    }

    private void initDrawerView() {
        WindowManager wm = getWindowManager();//获取屏幕宽高
        int width1 = wm.getDefaultDisplay().getWidth();
        int height1 = wm.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams para = drawerView.getLayoutParams();//获取drawerlayout的布局
        para.width = width1 * 5 / 8;//修改宽度
        para.height = height1;//修改高度
        drawerView.setLayoutParams(para); //设置修改后的布局。
    }


    @Override
    protected void updateData() {
        mWalletAdapter.setCurWalletId(mCurWalletId);
        List<WalletBean> beans = walletBeanDao.queryBuilder().list();
        mWalletAdapter.setData(beans);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.btn_drawer, R.id.btn_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_drawer:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_wallet:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateData(UpdateWalletListEvent event) {
        updateData();
    }
}
