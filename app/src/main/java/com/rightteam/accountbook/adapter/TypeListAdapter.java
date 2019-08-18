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

    private boolean mIsExpense;
    private int mCurType;


    public TypeListAdapter(Context context, boolean isExpense, int type) {
        super(context);
        mIsExpense = isExpense;
        mCurType = type;
    }

    public int getCurType(){
        return mCurType;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TypeListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_adapter_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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
            if(mCurType == position){
                iconType.setImageResource(mIsExpense ? ResDef.TYPE_ICONS_EX[position] : ResDef.TYPE_ICONS_IN[position]);
            }else {
                iconType.setImageResource(mIsExpense ? ResDef.TYPE_ICONS_GREY_EX[position] : ResDef.TYPE_ICONS_GREY_IN[position]);

            }
            textType.setText(mIsExpense ? ResDef.TYPE_NAMES_EX[position] : ResDef.TYPE_NAMES_IN[position]);
            itemView.setOnClickListener(v -> {
                mCurType = position;
                notifyDataSetChanged();
            });
        }
    }
}
