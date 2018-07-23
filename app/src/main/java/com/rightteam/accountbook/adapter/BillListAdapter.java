package com.rightteam.accountbook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rightteam.accountbook.R;
import com.rightteam.accountbook.base.BaseRvAdapter;
import com.rightteam.accountbook.bean.BillBean;

/**
 * Created by JasonWu on 7/21/2018
 */
public class BillListAdapter extends BaseRvAdapter<BillBean> {
    private final static int VIEW_TYPE_DATE = 0;
    private final static int VIEW_TYPE_BILL_TOP = 1;
    private final static int VIEW_TYPE_BILL_NORMAL = 2;

    public BillListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_DATE:
                layoutId = R.layout.view_adapter_date;
                break;
            default:
                layoutId = R.layout.view_adapter_bill;
                break;
        }
        return new BillListViewHolder(LayoutInflater.from(mContext).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BillListViewHolder) holder).bind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_DATE;
        } else {
            return VIEW_TYPE_BILL_NORMAL;
        }
    }

    class BillListViewHolder extends RecyclerView.ViewHolder {

        BillListViewHolder(View itemView) {
            super(itemView);
        }

        void bind(int position) {

        }
    }
}
