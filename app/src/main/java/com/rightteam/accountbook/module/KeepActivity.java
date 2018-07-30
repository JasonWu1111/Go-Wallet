package com.rightteam.accountbook.module;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.MainAdapter;
import com.rightteam.accountbook.base.BaseActivity;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.event.ModifyBillEvent;
import com.rightteam.accountbook.event.UpdateBillListEvent;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class KeepActivity extends BaseActivity {

    @BindView(R.id.text_calendar)
    TextView calendarText;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private String mCurDate;
    private long mCurWalletId;
    private long mCurBillId;
    private BillBean mCurBill;
    private MainAdapter mMainAdapter;
    private long mDateTimeMillis;
    private BillBeanDao billBeanDao = MyApplication.getsDaoSession().getBillBeanDao();

    @Override
    protected String getTypeface() {
        return "Roboto-Medium.ttf";
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_keep;
    }

    @Override
    protected void initViews() {
        handleIntent();

        List<String> titles = new ArrayList<>();
        titles.add("Expense");
        titles.add("Income");
        List<Fragment> fragments = new ArrayList<>();
        Fragment fragment1 = new KeepFragment();
        Fragment fragment2 = new KeepFragment();
        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        bundle1.putBoolean(KeyDef.IS_EXPENSE, true);
        bundle2.putBoolean(KeyDef.IS_EXPENSE, false);

        if (mCurBillId != -1) {
            if (mCurBill.getIsExpense()) {
                bundle1.putLong(KeyDef.BILL_ID, mCurBillId);
            } else {
                bundle2.putLong(KeyDef.BILL_ID, mCurBillId);
            }
        }

        fragment1.setArguments(bundle1);
        fragment2.setArguments(bundle2);
        fragments.add(fragment1);
        fragments.add(fragment2);
        mMainAdapter = new MainAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(mMainAdapter);
        tabLayout.setViewPager(viewPager);

        if (mCurBillId != -1) {
            if (mCurBill.getIsExpense()) {
                viewPager.setCurrentItem(0, true);
            } else {
                viewPager.setCurrentItem(1, true);
            }
        }
    }

    private void handleIntent() {
        mCurWalletId = getIntent().getLongExtra(KeyDef.WALLET_ID, -1);
        mCurBillId = getIntent().getLongExtra(KeyDef.BILL_ID, -1);
        if (mCurBillId == -1) {
            mDateTimeMillis = System.currentTimeMillis();
            calendarText.setText(CommonUtils.formatTimestamp(System.currentTimeMillis(), CommonUtils.DEFAULT_DAY_PATTERN));
        } else {
            mCurBill = billBeanDao.queryBuilder().where(BillBeanDao.Properties.Id.eq(mCurBillId)).unique();
            mDateTimeMillis = mCurBill.getTime();
            calendarText.setText(CommonUtils.formatTimestamp(mDateTimeMillis, CommonUtils.DEFAULT_DAY_PATTERN));
        }

    }

    @Override
    protected void updateData() {

    }

    public long getDateTimeMillis() {
        return mDateTimeMillis;
    }

    @OnClick({R.id.btn_close, R.id.btn_sure, R.id.text_calendar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_sure:
                if (((KeepFragment) mMainAdapter.getCurrentFragment()).CreateBill(mCurWalletId)) {
                    EventBus.getDefault().post(new UpdateBillListEvent(mCurWalletId));
                    if (mCurBillId != -1) {
                        EventBus.getDefault().post(new ModifyBillEvent());
                    }
                    finish();
                }
                break;
            case R.id.text_calendar:
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year1, month1, dayOfMonth) -> {
                    mCurDate = CommonUtils.formatDateSimple(year1, month1 + 1, dayOfMonth);
                    calendarText.setText(mCurDate);
                    Calendar c1 = Calendar.getInstance();
                    c1.set(year1, month1, dayOfMonth);
                    mDateTimeMillis = c1.getTimeInMillis();
                }, year, month, day);
                DatePicker dp = datePickerDialog.getDatePicker();
                dp.setMaxDate(new Date().getTime());
                datePickerDialog.show();
                break;
        }
    }

}
