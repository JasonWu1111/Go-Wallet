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
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.constants.ResDef;
import com.rightteam.accountbook.utils.CommonUtils;
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
    private final static int VIEW_TYPE_BLANK = 4;
    private final static int VIEW_TYPE_NULL = 5;

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
            case VIEW_TYPE_BLANK:
                layoutId = R.layout.view_adapter_blank;
                break;
            case VIEW_TYPE_NULL:
                layoutId = R.layout.view_adapter_null;
                break;
        }
        return new BillListViewHolder(LayoutInflater.from(mContext).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BillListViewHolder) holder).bind(position);
    }


    public void setData(List<BillBean> data, boolean isChosen) {
//        super.setData(data);
        itemTypes.clear();
        itemData.clear();

        if (isChosen) {
            if (data.size() == 0) {
                itemTypes.add(VIEW_TYPE_NULL);
                itemData.add("");
                notifyDataSetChanged();
                return;
            }
        } else {
            itemTypes.add(VIEW_TYPE_DATE);
            itemData.add("TODAY");

            if (data.size() == 0) {
                itemTypes.add(VIEW_TYPE_BILL_TODO);
                itemData.add("");
                notifyDataSetChanged();
                return;
            }
        }


        String date = "TODAY";
        boolean hasAddTodo = false;
        boolean isTodayFirst = true;
        for (BillBean billBean : data) {
            String curDate = CommonUtils.formatWithToday(billBean.getTime(), CommonUtils.WEEK_DAY_PATTERN);
            if (curDate.equals(date)) {
                hasAddTodo = true;
                if (isTodayFirst && date.equals("TODAY")) {
                    if(isChosen){
                        itemTypes.add(VIEW_TYPE_DATE);
                        itemData.add("TODAY");
                    }

                    itemTypes.add(VIEW_TYPE_BILL_TOP);
                    itemData.add(billBean);
                    isTodayFirst = false;
                } else {
                    itemTypes.add(VIEW_TYPE_BILL_NORMAL);
                    itemData.add(billBean);
                }
            } else {
                if (!hasAddTodo && !isChosen) {
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
        itemTypes.add(VIEW_TYPE_BLANK);
        itemData.add("");
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
            switch (itemTypes.get(position)) {
                case VIEW_TYPE_DATE:
                    TextView dateText = itemView.findViewById(R.id.text_cat);
                    dateText.setText((String) itemData.get(position));
                    break;
                case VIEW_TYPE_BILL_TOP:
                    View view = itemView.findViewById(R.id.view_top);
                    view.setVisibility(View.INVISIBLE);
                case VIEW_TYPE_BILL_NORMAL:
                    BillBean bean = (BillBean) itemData.get(position);
                    ImageView icon = itemView.findViewById(R.id.icon_bill);
                    TextView textType = itemView.findViewById(R.id.text_type);
                    TextView textPrice = itemView.findViewById(R.id.text_price);
                    icon.setImageResource(bean.getIsExpense() ? ResDef.TYPE_ICONS_EX[bean.getType()] : ResDef.TYPE_ICONS_IN[bean.getType()]);
                    textType.setText(bean.getIsExpense() ? ResDef.TYPE_NAMES_EX[bean.getType()] : ResDef.TYPE_NAMES_IN[bean.getType()]);
                    textPrice.setTextColor(mContext.getResources().getColor(bean.getIsExpense() ? R.color.orange : R.color.green));
                    String price = CommonUtils.formatPriceWithSource(bean.getPrice(), bean.getIsExpense());
                    textPrice.setText(price);

                    itemView.setOnClickListener(v -> {
                        onItemClickListener.onClick(position, KeyDef.JUMP_TO_DETAIL, ((BillBean) itemData.get(position)).getId());
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
