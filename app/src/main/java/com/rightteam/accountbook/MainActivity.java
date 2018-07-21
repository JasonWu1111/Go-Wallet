package com.rightteam.accountbook;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.rightteam.accountbook.adapter.MainAdapter;
import com.rightteam.accountbook.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.fab)
//    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
//        setSupportActionBar(toolbar);

//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);
        List<String> titles = new ArrayList<>();
        titles.add("Transaction");
        titles.add("Statements");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TransactionFragment());
        fragments.add(new Fragment());
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(mainAdapter);
        tabLayout.setupWithViewPager(viewPager);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
