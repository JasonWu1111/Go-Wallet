package com.rightteam.accountbook.module;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.BillListAdapter;
import com.rightteam.accountbook.adapter.TypeListAdapter;
import com.rightteam.accountbook.base.BaseFragment;
import com.rightteam.accountbook.bean.BillBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by JasonWu on 7/22/2018
 */
public class ExpenseFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_expense;
    }

    @Override
    protected void initViews() {
        TypeListAdapter adapter = new TypeListAdapter(getContext());
        List<BillBean> beans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            beans.add(new BillBean());
        }
        adapter.setData(beans);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void updateData() {

    }

    @OnClick(R.id.btn_type)
    public void onViewClicked(View v) {
        showPopup(v);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.bill_type, popup.getMenu());
        popup.show();
    }
}
