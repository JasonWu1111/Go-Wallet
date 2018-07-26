package com.rightteam.accountbook.module;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.TypeListAdapter;
import com.rightteam.accountbook.base.BaseFragment;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.bean.TypeBean;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.utils.AssetsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JasonWu on 7/22/2018
 */
public class ExpenseFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.text_cat)
    TextView textCat;
    @BindView(R.id.edit_price)
    EditText editPrice;
    @BindView(R.id.edit_memo)
    EditText editMemo;

    private int mCurType;
    private String mCurCat;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_expense;
    }

    @Override
    protected void initViews() {
        mCurCat = "Cash";
        List<TypeBean> beans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TypeBean typeBean = new TypeBean();
            beans.add(typeBean);
        }

        TypeListAdapter adapter = new TypeListAdapter(getContext());
        adapter.setData(beans);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void updateData() {

    }

    @OnClick(R.id.btn_cat)
    public void onViewClicked(View v) {
        showPopup(v);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.bill_cat2, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            mCurCat = item.getTitle().toString();
            textCat.setText(mCurCat);
            return true;
        });
        popup.show();
    }

    public void CreateBill(long walletId) {
        BillBeanDao billDao = MyApplication.getsDaoSession().getBillBeanDao();
        BillBean bill = new BillBean(null, Float.valueOf(editPrice.getEditableText().toString())
                , System.currentTimeMillis()
                , mCurCat
                , new Random().nextInt(10)
                , walletId
                , editMemo.getEditableText().toString()
                , true);
        billDao.insert(bill);
    }

    public void UpdateBill(long walletId) {
        BillBeanDao billDao = MyApplication.getsDaoSession().getBillBeanDao();
    }
}
