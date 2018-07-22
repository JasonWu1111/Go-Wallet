package com.rightteam.accountbook.module;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.MainAdapter;
import com.rightteam.accountbook.base.BaseActivity;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.greendao.BillBeanDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class KeepActivity extends BaseActivity {

    @BindView(R.id.calendar_text)
    TextView calendarText;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private String mCurDate;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_keep;
    }

    @Override
    protected void initViews() {
        List<String> titles = new ArrayList<>();
        titles.add("Expense");
        titles.add("Income");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ExpenseFragment());
        fragments.add(new Fragment());
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(mainAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void updateData() {

    }

    @OnClick({R.id.btn_close, R.id.btn_sure, R.id.btn_calendar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_sure:
                BillBeanDao billDao = MyApplication.getsDaoSession().getBillBeanDao();
                BillBean bill = new BillBean(null, 200.00f, System.currentTimeMillis(), 1, 1, 1L, "");
                billDao.insert(bill);
                finish();
                break;
            case R.id.btn_calendar:
                View dialogView = View.inflate(this, R.layout.dialog_calendar, null);
                final DatePicker datePicker = dialogView.findViewById(R.id.view_calendar);
                new AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("OK", (dialog, which) -> {
                            mCurDate = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
                            calendarText.setText(mCurDate);
                            dialog.dismiss();
                        })
                        .create()
                        .show();
                break;
        }
    }

}
