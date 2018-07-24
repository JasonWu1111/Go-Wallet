package com.rightteam.accountbook.module;

import android.content.Intent;
import android.os.Debug;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.BillListAdapter;
import com.rightteam.accountbook.base.BaseFragment;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.bean.WalletBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.greendao.WalletBeanDao;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by JasonWu on 7/21/2018
 */
public class TransactionFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Long mCurWalletId;
    private BillListAdapter mAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_transaction;
    }

    @Override
    protected void initViews() {
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
        WalletBeanDao walletDao = MyApplication.getsDaoSession().getWalletBeanDao();
        WalletBean walletBean = walletDao.queryBuilder().where(WalletBeanDao.Properties.Id.eq(mCurWalletId)).unique();
        List<BillBean> beans = walletBean.getBills();

        if (beans.size() > 2) {
            Collections.sort(beans, (b1, b2) -> Long.compare(b2.getTime(), b1.getTime()));
        }

        mAdapter.setData(beans);
    }

    private void JumpToKeep() {
        Intent intent = new Intent(getContext(), KeepActivity.class);
        intent.putExtra(KeyDef.WALLET_ID, mCurWalletId);
        startActivityForResult(intent, KeyDef.INTENT_REQUEST_CODE_ACTION_KEEP);
    }

    @OnClick(R.id.btn_fab)
    public void onViewClicked(View v) {
        JumpToKeep();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case KeyDef.INTENT_REQUEST_CODE_ACTION_KEEP:
                    updateData();
                    break;
            }
        }
    }
}
