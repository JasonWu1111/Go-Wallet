package com.rightteam.accountbook.module;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;

import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.BillListAdapter;
import com.rightteam.accountbook.base.BaseFragment;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.module.KeepActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JasonWu on 7/21/2018
 */
public class TransactionFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_transaction;
    }

    @Override
    protected void initViews() {
        BillListAdapter adapter = new BillListAdapter(getContext());
        BillBeanDao billDao = MyApplication.getsDaoSession().getBillBeanDao();
        List<BillBean> beans = billDao.queryBuilder().list();
        adapter.setData(beans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void updateData() {

    }

    @OnClick(R.id.btn_fab)
    public void onViewClicked(View v) {
        startActivity(new Intent(getContext(), KeepActivity.class));

    }
}
