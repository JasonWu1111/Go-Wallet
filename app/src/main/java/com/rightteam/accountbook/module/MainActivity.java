package com.rightteam.accountbook.module;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.MainAdapter;
import com.rightteam.accountbook.adapter.WalletListAdapter;
import com.rightteam.accountbook.base.BaseActivity;
import com.rightteam.accountbook.bean.WalletBean;
import com.rightteam.accountbook.greendao.WalletBeanDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.btn_wallet)
    ImageView btnWallet;

    private boolean mIsWalletShow = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {


//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initDrawerView();

        List<String> titles = new ArrayList<>();
        titles.add("Transaction");
        titles.add("Statements");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TransactionFragment());
        fragments.add(new Fragment());
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(mainAdapter);
        tabLayout.setViewPager(viewPager);
    }

    private void initDrawerView() {
        WalletListAdapter adapter = new WalletListAdapter(this);
        WalletBeanDao walletBeanDao = MyApplication.getsDaoSession().getWalletBeanDao();
        List<WalletBean> beans = walletBeanDao.queryBuilder().list();
        if (beans.size() == 0) {
            WalletBean bean = new WalletBean(null, 0, "Wallet1", System.currentTimeMillis());
            walletBeanDao.insert(bean);
            beans = walletBeanDao.queryBuilder().list();
        }
        adapter.setData(beans);
        adapter.setOnItemClickListener((position, action, data) -> drawerLayout.closeDrawer(GravityCompat.START));
        walletList.setLayoutManager(new LinearLayoutManager(this));
        walletList.setAdapter(adapter);
    }

    @Override
    protected void updateData() {

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
}
