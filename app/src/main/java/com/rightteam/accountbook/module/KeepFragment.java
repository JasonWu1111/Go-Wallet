package com.rightteam.accountbook.module;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.TypeListAdapter;
import com.rightteam.accountbook.base.BaseFragment;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.bean.TypeBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.utils.CommonUtils;
import com.rightteam.accountbook.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JasonWu on 7/22/2018
 */
public class KeepFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.text_cat)
    TextView textCat;
    @BindView(R.id.edit_price)
    EditText editPrice;
    @BindView(R.id.edit_memo)
    EditText editMemo;
    @BindView(R.id.text_dollar)
    TextView textDollar;

    private long mCurBillId = -1;
    private BillBean mCurBill;
    private String mCurCat;
    private TypeListAdapter mAdapter;
    private boolean mIsExpense;
    private BillBeanDao billBeanDao = MyApplication.getsDaoSession().getBillBeanDao();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_keep;
    }

    @Override
    protected void initViews() {
        mCurCat = "Cash";
        if (getArguments() != null) {
            mIsExpense = getArguments().getBoolean(KeyDef.IS_EXPENSE);
            mCurBillId = getArguments().getLong(KeyDef.BILL_ID, -1);
        }

        textDollar.setText(mIsExpense ? "- $" : "+ $");

        if (mCurBillId != -1) {
            mCurBill = billBeanDao.queryBuilder().where(BillBeanDao.Properties.Id.eq(mCurBillId)).unique();
            mCurCat = mCurBill.getCategory();
            textCat.setText(mCurCat);
            editMemo.setText(mCurBill.getMemo());
            editPrice.setText(CommonUtils.formatNumberWithComma(mCurBill.getPrice()));
        }

        List<TypeBean> beans = new ArrayList<>();
        for (int i = 0; i < (mIsExpense ? 10 : 4); i++) {
            TypeBean typeBean = new TypeBean();
            beans.add(typeBean);
        }

        mAdapter = new TypeListAdapter(getContext(), mIsExpense, mCurBillId != -1 ? mCurBill.getType() : -1);
        mAdapter.setData(beans);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void updateData() {

    }

    @OnClick(R.id.btn_cat)
    public void onViewClicked(View v) {
        showPopup(v);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(Objects.requireNonNull(getContext()), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.bill_cat2, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            mCurCat = item.getTitle().toString();
            textCat.setText(mCurCat);
            return true;
        });
        popup.show();
    }

    public boolean CreateBill(long walletId) {
        String price = editPrice.getEditableText().toString();

        if(price.equals("")){
            ToastUtil.showToast("Please enter the amount.");
            return false;
        }

        if(mAdapter.getCurType() == -1){
            ToastUtil.showToast("Please select the classification.");
            return false;
        }

        BillBean bill = new BillBean(mCurBillId != -1 ? mCurBillId : null
                , Float.valueOf(price)
                , ((KeepActivity) getActivity()).getDateTimeMillis()
                , mCurCat
                , mAdapter.getCurType()
                , walletId
                , editMemo.getEditableText().toString()
                , mIsExpense);
        if(mCurBillId != -1){
            billBeanDao.update(bill);
        }else {
            billBeanDao.insert(bill);
        }
        return true;
    }
}
