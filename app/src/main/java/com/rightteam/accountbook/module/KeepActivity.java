package com.rightteam.accountbook.module;

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
//                setResult(RESULT_OK);
                finish();
                break;
            case R.id.btn_sure:
                ((ExpenseFragment)mMainAdapter.getCurrentFragment()).CreateBill(mCurWalletId);
//                setResult(RESULT_OK);
                EventBus.getDefault().post(new UpdateBillListEvent());
                finish();
                break;
            case R.id.text_calendar:
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
