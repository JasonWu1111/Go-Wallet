package com.rightteam.accountbook.module;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.BillListAdapter;
import com.rightteam.accountbook.base.BaseFragment;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.event.UpdateBillListEvent;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.utils.CommonUtils;
import com.rightteam.accountbook.utils.DensityUtil;
import com.shawnlin.numberpicker.NumberPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JasonWu on 7/21/2018
 */
public class TransactionFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.text_expense_total)
    TextView exTolText;
    @BindView(R.id.text_income_total)
    TextView inTolText;
    @BindView(R.id.text_cat)
    TextView textCat;
    @BindView(R.id.text_date)
    TextView textDate;

    private Long mCurWalletId;
    private BillListAdapter mAdapter;
    private float mExTotal;
    private float mInTotal;
    private BillBeanDao billBeanDao;
    private String mCurCat;
    private int mCurYear;
    private int mCurMonth;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_transaction;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);

        mCurCat = "All";
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        mCurYear = year;
        mCurMonth = month;
        textDate.setText(CommonUtils.formatDateSimple(year, month + 1));

        billBeanDao = MyApplication.getsDaoSession().getBillBeanDao();
        mCurWalletId = getArguments() != null ? getArguments().getLong(KeyDef.WALLET_ID) : -1;
        mAdapter = new BillListAdapter(getContext());
        mAdapter.setOnItemClickListener((position, action, id) -> {
            switch (action) {
                case KeyDef.JUMP_TO_DETAIL:
                    JumpToDetail(id);
                    break;
                case KeyDef.JUMP_TO_KEEP:
                    JumpToKeep();
                    break;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void updateData() {
//        WalletBeanDao walletDao = MyApplication.getsDaoSession().getWalletBeanDao();
//        WalletBean walletBean = walletDao.queryBuilder().where(WalletBeanDao.Properties.Id.eq(mCurWalletId)).unique();
//        List<BillBean> beans = walletBean.getBills();
        mExTotal = 0f;
        mInTotal = 0f;

        WhereCondition condition1 = BillBeanDao.Properties.WalletId.eq(mCurWalletId);
        WhereCondition condition2 = BillBeanDao.Properties.Category.eq(mCurCat);
        WhereCondition condition3 = BillBeanDao.Properties.Time.between(
                CommonUtils.transformStartMonthToMillis(mCurYear, mCurMonth)
                , CommonUtils.transformEndMonthToMillis(mCurYear, mCurMonth));

        boolean isChosen = false;
        List<BillBean> beans;
        if (mCurCat.equals("All")) {
            if (isCurrentMonth(mCurYear, mCurMonth)) {
                beans = billBeanDao.queryBuilder().where(condition1).list();
            } else {
                beans = billBeanDao.queryBuilder().where(condition1, condition3).list();
                isChosen = true;
            }
        } else {
            beans = billBeanDao.queryBuilder().where(condition1, condition2, condition3).list();
            isChosen = true;
        }


        for (BillBean bean : beans) {
            if (bean.getIsExpense()) {
                mExTotal += bean.getPrice();
            } else {
                mInTotal += bean.getPrice();
            }
        }
        exTolText.setText("$" + CommonUtils.formatNumberWithComma(mExTotal));
        inTolText.setText("$" + CommonUtils.formatNumberWithComma(mInTotal));

        if (beans.size() >= 2) {
            Collections.sort(beans, (b1, b2) -> Long.compare(b2.getTime(), b1.getTime()));
        }

        mAdapter.setData(beans, isChosen);
    }

    private boolean isCurrentMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH);
        return year1 == year && month1 == month;
    }

    private void JumpToDetail(long billId) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(KeyDef.WALLET_ID, mCurWalletId);
        intent.putExtra(KeyDef.BILL_ID, billId);
        startActivity(intent);
    }

    private void JumpToKeep() {
        Intent intent = new Intent(getContext(), KeepActivity.class);
        intent.putExtra(KeyDef.WALLET_ID, mCurWalletId);
        startActivity(intent);
    }

    @OnClick({R.id.btn_fab, R.id.btn_choose_cat, R.id.btn_choose_date})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_fab:
                JumpToKeep();
                break;
            case R.id.btn_choose_cat:
                showPopup(v);
                break;
            case R.id.btn_choose_date:
                showDateDialog();
                break;
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.bill_cat, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            mCurCat = item.getTitle().toString();
            textCat.setText(mCurCat);
            updateData();
            return true;
        });
        popup.show();
    }

    public void showDateDialog() {
        View view = View.inflate(getContext(), R.layout.dialog_calendar, null);
        ImageView btnClose = view.findViewById(R.id.btn_close);
        TextView btnOk = view.findViewById(R.id.btn_ok);
        NumberPicker monthPicker = view.findViewById(R.id.pick_month);
        NumberPicker yearPicker = view.findViewById(R.id.pick_year);
        monthPicker.setValue(mCurMonth + 1);
        yearPicker.setValue(mCurYear);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();
        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
        });
        btnOk.setOnClickListener(v -> {
            if (mCurYear != yearPicker.getValue() || mCurMonth != monthPicker.getValue() - 1) {
                mCurYear = yearPicker.getValue();
                mCurMonth = monthPicker.getValue() - 1;
                textDate.setText(CommonUtils.formatDateSimple(mCurYear, mCurMonth + 1));
                updateData();
            }
            dialog.dismiss();
        });
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = DensityUtil.dp2px(getContext(), 300);
        dialog.getWindow().setAttributes(params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateData(UpdateBillListEvent event) {
        mCurWalletId = event.getWalletId();
        updateData();
    }
}

