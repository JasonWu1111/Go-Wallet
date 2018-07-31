package com.rightteam.accountbook.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.rightteam.accountbook.adapter.OnItemClickListener;

import java.util.List;

/**
 * Created by JasonWu on 28/12/2017
 */

public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter {
    protected List<T> mData;
    public Context mContext;
    public OnItemClickListener onItemClickListener;

    public BaseRvAdapter(Context context) {
        mContext = context;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        mData.addAll(data);
        notifyItemRangeChanged(mData.size(), data.size());
    }

    public void deleteItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, mData.size());
    }

    public void deleteAll(){
        mData = null;
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
