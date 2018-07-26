package com.rightteam.accountbook.module;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.MainAdapter;
import com.rightteam.accountbook.adapter.WalletListAdapter;
import com.rightteam.accountbook.base.BaseActivity;
import com.rightteam.accountbook.bean.WalletBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.greendao.WalletBeanDao;

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

    private boolean mIsWalletShow = true;
    private long mCurWalletId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        initDrawerView();

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
    }

    private void initDrawerView() {
        WindowManager wm = getWindowManager();//获取屏幕宽高
        int width1 = wm.getDefaultDisplay().getWidth();
        int height1 = wm.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams para = drawerView.getLayoutParams();//获取drawerlayout的布局
        para.width = width1 * 5 / 8;//修改宽度
        para.height = height1;//修改高度
        drawerView.setLayoutParams(para); //设置修改后的布局。

        WalletListAdapter adapter = new WalletListAdapter(this);
        WalletBeanDao walletBeanDao = MyApplication.getsDaoSession().getWalletBeanDao();
        List<WalletBean> beans = walletBeanDao.queryBuilder().list();
        if (beans.size() == 0) {
            WalletBean bean = new WalletBean(null, 0, "Wallet1", System.currentTimeMillis());
            walletBeanDao.insert(bean);
            mCurWalletId = bean.getId();
            beans = walletBeanDao.queryBuilder().list();
        } else {
            mCurWalletId = beans.get(0).getId();
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
