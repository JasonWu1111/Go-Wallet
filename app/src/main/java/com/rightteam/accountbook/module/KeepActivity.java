package com.rightteam.accountbook.module;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.MainAdapter;
import com.rightteam.accountbook.base.BaseActivity;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.event.UpdateBillListEvent;
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
    private MainAdapter mMainAdapter;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_keep;
    }

    @Override
    protected void initViews() {
        mCurWalletId = getIntent().getLongExtra(KeyDef.WALLET_ID, -1);
        mCurBillId = getIntent().getLongExtra(KeyDef.BILL_ID, -1);
        calendarText.setText(CommonUtils.formatTimestamp(System.currentTimeMillis(), CommonUtils.DEFAULT_DAY_PATTERN));

        List<String> titles = new ArrayList<>();
        titles.add("Expense");
        titles.add("Income");
        List<Fragment> fragments = new ArrayList<>();
        Fragment fragment = new ExpenseFragment();
        fragments.add(fragment);
        fragments.add(new Fragment());
        mMainAdapter = new MainAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(mMainAdapter);
        tabLayout.setViewPager(viewPager);
    }

    @Override
    protected void updateData() {

    }

    @OnClick({R.id.btn_close, R.id.btn_sure, R.id.text_calendar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_sure:
                ((ExpenseFragment) mMainAdapter.getCurrentFragment()).CreateBill(mCurWalletId);
                EventBus.getDefault().post(new UpdateBillListEvent());
                finish();
                break;
            case R.id.text_calendar:
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year1, month1, dayOfMonth) -> {
                    mCurDate = (month1 + 1) + "/" + dayOfMonth + "/" + year1;
                    calendarText.setText(mCurDate);
                }, year, month, day);
                DatePicker dp = datePickerDialog.getDatePicker();
                dp.setMaxDate(new Date().getTime());
                datePickerDialog.show();
        }
    }

}
