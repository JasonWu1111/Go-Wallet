package com.rightteam.accountbook.module;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.BillListAdapter;
import com.rightteam.accountbook.base.BaseFragment;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.event.ChangeWalletEvent;
import com.rightteam.accountbook.event.UpdateBillListEvent;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    private Long mCurWalletId;
    private BillListAdapter mAdapter;
    private float mExTotal;
    private float mInTotal;
    private String mCurCat;
    private BillBeanDao billBeanDao;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_transaction;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        billBeanDao = MyApplication.getsDaoSession().getBillBeanDao();
        mCurWalletId = getArguments() != null ? getArguments().getLong(KeyDef.WALLET_ID) : -1;
        mAdapter = new BillListAdapter(getContext());
        mAdapter.setOnItemClickListener((position, action, id) -> {
            switch (action) {
                case KeyDef.JUMP_TO_DETAIL:
                    startActivity(new Intent(getContext(), DetailActivity.class).putExtra(KeyDef.BILL_ID, id));
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

        List<BillBean> beans = billBeanDao.queryBuilder().where(BillBeanDao.Properties.WalletId.eq(mCurWalletId)).list();
        for (BillBean bean : beans) {
            if (bean.getIsExpense()) {
                mExTotal += bean.getPrice();
            } else {
                mInTotal += bean.getPrice();
            }
        }
        exTolText.setText("$" + CommonUtils.formatNumberWithComma(mExTotal));
        inTolText.setText("$" + CommonUtils.formatNumberWithComma(mInTotal));

        if (beans.size() > 2) {
            Collections.sort(beans, (b1, b2) -> Long.compare(b2.getTime(), b1.getTime()));
        }

        mAdapter.setData(beans);
    }

    private void JumpToKeep() {
        Intent intent = new Intent(getContext(), KeepActivity.class);
        intent.putExtra(KeyDef.WALLET_ID, mCurWalletId);
        startActivity(intent);
    }

    @OnClick({R.id.btn_fab, R.id.btn_choose_cat})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_fab:
                JumpToKeep();
                break;
            case R.id.btn_choose_cat:
                showPopup(v);
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
            return true;
        });
        popup.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateData(UpdateBillListEvent event) {
        updateData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWalletChange(ChangeWalletEvent event) {
        mCurWalletId = event.getWalletId();
        updateData();
    }
}

