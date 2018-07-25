package com.rightteam.accountbook.module;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.base.BaseActivity;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.constants.ResDef;
import com.rightteam.accountbook.event.UpdateBillListEvent;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

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
    private BillBean mBill;
    private BillBeanDao billBeanDao;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initViews() {
        mCurBillId = getIntent().getLongExtra(KeyDef.BILL_ID, -1L);
        billBeanDao = MyApplication.getsDaoSession().getBillBeanDao();
        mBill = billBeanDao.queryBuilder().where(BillBeanDao.Properties.Id.eq(mCurBillId)).unique();
        iconType.setImageResource(ResDef.TYPE_ICONS2[mBill.getType()]);
        textType.setText(ResDef.TYPE_NAMES[mBill.getType()]);
        textPrice.setText(CommonUtils.formatPriceWithSource(mBill.getPrice(), mBill.getIsExpense()));
        textCat.setText(mBill.getCategory());
        textDate.setText(CommonUtils.formatTimestamp(mBill.getTime(), CommonUtils.DEFAULT_DAY_PATTERN));
        textMemo.setText(mBill.getMemo());
    }

    @Override
    protected void updateData() {

    }

    @OnClick({R.id.btn_back, R.id.btn_modify, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_modify:

                break;
            case R.id.btn_delete:
                billBeanDao.delete(mBill);
                EventBus.getDefault().post(new UpdateBillListEvent());
                finish();
                break;
        }
    }
}
