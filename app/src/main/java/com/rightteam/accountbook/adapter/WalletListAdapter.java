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
import com.rightteam.accountbook.bean.WalletBean;

/**
 * Created by JasonWu on 7/21/2018
 */
public class WalletListAdapter extends BaseRvAdapter<WalletBean> {
    private final static int VIEW_TYPE_WALLET = 0;
    private final static int VIEW_TYPE_ADD = 1;

    public WalletListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layId = viewType == VIEW_TYPE_WALLET ? R.layout.view_adapter_wallet : R.layout.view_adapter_wallet_add;
        return new WalletViewHolder(LayoutInflater.from(mContext).inflate(layId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((WalletViewHolder)holder).bind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount() - 1){
            return VIEW_TYPE_ADD;
        }else {
            return VIEW_TYPE_WALLET;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    class WalletViewHolder extends RecyclerView.ViewHolder{

        WalletViewHolder(View itemView) {
            super(itemView);
        }

        void bind(int position){

        }
    }
}
