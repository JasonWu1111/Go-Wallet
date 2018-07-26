package com.rightteam.accountbook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightteam.accountbook.R;
import com.rightteam.accountbook.base.BaseRvAdapter;
import com.rightteam.accountbook.bean.TypeBean;
import com.rightteam.accountbook.constants.ResDef;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JasonWu on 7/21/2018
 */
public class TypeListAdapter extends BaseRvAdapter<TypeBean> {

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
        ((TypeListViewHolder) holder).bind(position);
    }

    class TypeListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon_type)
        ImageView iconType;
        @BindView(R.id.text_cat)
        TextView textType;

        TypeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            iconType.setImageResource(ResDef.TYPE_ICONS[position]);
//            iconType.setImageResource(R.drawable.travel);
            textType.setText(ResDef.TYPE_NAMES[position]);
        }
    }
}
