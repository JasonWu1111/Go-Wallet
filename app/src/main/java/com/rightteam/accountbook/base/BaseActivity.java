package com.rightteam.accountbook.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JasonWu on 27/12/2017
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        initViews();
        updateData();
    }

    public String getTAG(){
        return this.getClass().getSimpleName();
    }

    protected abstract int getLayoutResId();

    protected abstract void initViews();

    protected abstract void updateData();
}
