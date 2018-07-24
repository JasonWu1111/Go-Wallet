package com.rightteam.accountbook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rightteam.accountbook.R;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.utils.DateUtils;
import com.rightteam.accountbook.base.BaseRvAdapter;
import com.rightteam.accountbook.bean.BillBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JasonWu on 7/21/2018
 */
public class BillListAdapter extends BaseRvAdapter<BillBean> {
    private final static int VIEW_TYPE_DATE = 0;
    private final static int VIEW_TYPE_BILL_TOP = 1;
    private final static int VIEW_TYPE_BILL_NORMAL = 2;
    private final static int VIEW_TYPE_BILL_TODO = 3;

    private List<Integer> itemTypes = new ArrayList<>();
    private List<Object> itemData = new ArrayList<>();

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
            case VIEW_TYPE_BILL_TOP:
                layoutId = R.layout.view_adapter_bill;
                break;
            case VIEW_TYPE_BILL_NORMAL:
                layoutId = R.layout.view_adapter_bill;
                break;
            case VIEW_TYPE_BILL_TODO:
                layoutId = R.layout.view_adapter_todo;
                break;
        }
        return new BillListViewHolder(LayoutInflater.from(mContext).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BillListViewHolder) holder).bind(position);
    }

    @Override
    public void setData(List<BillBean> data) {
//        super.setData(data);
        itemTypes.clear();
        itemData.clear();
        itemTypes.add(VIEW_TYPE_DATE);
        itemData.add("TODAY");

        if(data.size() == 0){
            itemTypes.add(VIEW_TYPE_BILL_TODO);
            itemData.add("");
            return;
        }

        String date = "TODAY";
        boolean hasAddTodo = false;
        boolean isTodayFirst = true;
        for (BillBean billBean : data) {
            String curDate = DateUtils.formatWithToday(billBean.getTime(), DateUtils.WEEK_DAY_PATTERN);
            if (curDate.equals(date)) {
                hasAddTodo = true;
                if (isTodayFirst && date.equals("TODAY")) {
                    itemTypes.add(VIEW_TYPE_BILL_TOP);
                    itemData.add(billBean);
                    isTodayFirst = false;
                } else {
                    itemTypes.add(VIEW_TYPE_BILL_NORMAL);
                    itemData.add(billBean);
                }
            } else {
                if (!hasAddTodo) {
                    itemTypes.add(VIEW_TYPE_BILL_TODO);
                    itemData.add("");
                    hasAddTodo = true;
                }

                date = curDate;
                itemTypes.add(VIEW_TYPE_DATE);
                itemData.add(date);
                itemTypes.add(VIEW_TYPE_BILL_TOP);
                itemData.add(billBean);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemTypes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemTypes.get(position);
    }

    class BillListViewHolder extends RecyclerView.ViewHolder {

        BillListViewHolder(View itemView) {
            super(itemView);
        }

        void bind(int position) {
            switch ( itemTypes.get(position)){
                case VIEW_TYPE_DATE:
                    TextView dateText = itemView.findViewById(R.id.text_cat);
                    dateText.setText((String)itemData.get(position));
                    break;
                case VIEW_TYPE_BILL_TOP:
                case VIEW_TYPE_BILL_NORMAL:
                    itemView.setOnClickListener(v -> {
                        onItemClickListener.onClick(position, KeyDef.JUMP_TO_DETAIL, ((BillBean)itemData.get(position)).getId());
                    });
                    break;
                case VIEW_TYPE_BILL_TODO:
                    itemView.setOnClickListener(v -> {
                        onItemClickListener.onClick(position, KeyDef.JUMP_TO_KEEP, -1L);
                    });
                    break;
            }
        }

    }
}
