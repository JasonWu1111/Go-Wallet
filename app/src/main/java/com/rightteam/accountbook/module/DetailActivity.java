package com.rightteam.accountbook.module;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.base.BaseActivity;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.constants.ResDef;
import com.rightteam.accountbook.event.ModifyBillEvent;
import com.rightteam.accountbook.event.UpdateBillListEvent;
import com.rightteam.accountbook.event.UpdateWalletListEvent;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.icon_type)
    ImageView iconType;
    @BindView(R.id.text_type)
    TextView textType;
    @BindView(R.id.text_price)
    TextView textPrice;
    @BindView(R.id.text_cat)
    TextView textCat;
    @BindView(R.id.text_date)
    TextView textDate;
    @BindView(R.id.text_memo)
    TextView textMemo;

    private long mCurBillId;
    private long mCurWalletId;
    private BillBean mBill;
    private BillBeanDao billBeanDao;


    @Override
    protected String getTypeface() {
        return "Roboto-Regular.ttf";
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        mCurBillId = getIntent().getLongExtra(KeyDef.BILL_ID, -1);
        mCurWalletId = getIntent().getLongExtra(KeyDef.WALLET_ID, -1);
        billBeanDao = MyApplication.getsDaoSession().getBillBeanDao();
    }

    @Override
    protected void updateData() {
        mBill = billBeanDao.queryBuilder().where(BillBeanDao.Properties.Id.eq(mCurBillId)).unique();
        iconType.setImageResource(mBill.getIsExpense() ? ResDef.TYPE_ICONS2_EX[mBill.getType()] : ResDef.TYPE_ICONS2_IN[mBill.getType()]);
        textType.setText(ResDef.TYPE_NAMES_EX[mBill.getType()]);
        textPrice.setText(CommonUtils.formatPriceWithSource(mBill.getPrice(), mBill.getIsExpense()));
        textCat.setText(mBill.getCategory());
        textDate.setText(CommonUtils.formatTimestamp(mBill.getTime(), CommonUtils.DEFAULT_DAY_PATTERN));
        textMemo.setText(mBill.getMemo());
    }

    private void onModifyBill() {
        Intent intent = new Intent(this, KeepActivity.class);
        intent.putExtra(KeyDef.WALLET_ID, mCurWalletId);
        intent.putExtra(KeyDef.BILL_ID, mCurBillId);
        startActivity(intent);
    }

    @OnClick({R.id.btn_back, R.id.btn_modify, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_modify:
                onModifyBill();
                break;
            case R.id.btn_delete:
                new AlertDialog.Builder(this)
                        .setTitle("Delete this record?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            billBeanDao.delete(mBill);
                            EventBus.getDefault().post(new UpdateBillListEvent(mCurWalletId));
                            dialog.dismiss();
                            finish();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create()
                        .show();

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateBill(ModifyBillEvent event) {
        updateData();
    }
}
