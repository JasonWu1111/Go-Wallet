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
import com.rightteam.accountbook.bean.BillPerBean;
import com.rightteam.accountbook.constants.ResDef;
import com.rightteam.accountbook.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JasonWu on 8/4/2018
 */
public class BillPerAdapter extends BaseRvAdapter<BillPerBean> {
    private static final int VIEW_TYPE_NO_RECORD = 0;
    private static final int VIEW_TYPE_NO_NORMAL = 1;


    public BillPerAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NO_RECORD) {
            return new BaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_adapter_no_record, parent, false));
        }
        return new BillPerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_adapter_per, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_NO_NORMAL){
            ((BillPerViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return getData() == null || getData().size() == 0 ? 1 : getData().size();
    }

    @Override
    public int getItemViewType(int position) {
        return getData() == null || getData().size() == 0 ? VIEW_TYPE_NO_RECORD : VIEW_TYPE_NO_NORMAL;
    }

    class BillPerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon_type)
        ImageView iconType;
        @BindView(R.id.text_type)
        TextView textType;
        @BindView(R.id.text_price)
        TextView textPrice;
        @BindView(R.id.text_per)
        TextView textPer;

        BillPerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            BillPerBean bean = getData().get(position);
            iconType.setImageResource(bean.isExpense() ? ResDef.TYPE_ICONS_EX[bean.getType()] : ResDef.TYPE_ICONS_IN[bean.getType()]);
            textType.setText(bean.isExpense() ? ResDef.TYPE_NAMES_EX[bean.getType()] : ResDef.TYPE_NAMES_IN[bean.getType()]);
            textPrice.setText(String.format("$%s", CommonUtils.formatNumberWithComma(bean.getPrice())));
            textPer.setText(String.format("%s%%", CommonUtils.formatNumberPercent(bean.getPer())));
        }
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(View itemView) {
            super(itemView);
        }
    }
}
