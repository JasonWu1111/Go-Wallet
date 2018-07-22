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
public class TypeListAdapter extends BaseRvAdapter<BillBean> {

    public TypeListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TypeListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_adapter_type, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TypeListViewHolder)holder).bind(position);
    }

    class TypeListViewHolder extends RecyclerView.ViewHolder{

        TypeListViewHolder(View itemView) {
            super(itemView);
        }

        void bind(int position){

        }
    }
}
