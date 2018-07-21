package com.rightteam.accountbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.rightteam.accountbook.base.BaseRvAdapter;
import com.rightteam.accountbook.bean.BillBean;

/**
 * Created by JasonWu on 7/21/2018
 */
public class BillListAdapter extends BaseRvAdapter<BillBean> {
    public BillListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
