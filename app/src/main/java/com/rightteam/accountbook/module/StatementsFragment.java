package com.rightteam.accountbook.module;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rightteam.accountbook.R;
import com.rightteam.accountbook.base.BaseFragment;
import com.rightteam.accountbook.ui.RingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JasonWu on 7/31/2018
 */
public class StatementsFragment extends BaseFragment {
    @BindView(R.id.ring_view)
    RingView ringView;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_statements;
    }

    @Override
    protected void initViews() {
        List<Float> pers = new ArrayList<>();
        pers.add(0.21f);
        pers.add(0.15f);
        pers.add(0.35f);
        pers.add(0.10f);
        ringView.setPers(pers);
    }

    @Override
    protected void updateData() {

    }
}
