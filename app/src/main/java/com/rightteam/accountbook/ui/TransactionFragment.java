package com.rightteam.accountbook.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.BillListAdapter;
import com.rightteam.accountbook.base.BaseFragment;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.constants.ResDef;
import com.rightteam.accountbook.event.UpdateBillListEvent;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.utils.CommonUtils;
import com.rightteam.accountbook.utils.DensityUtil;
import com.shawnlin.numberpicker.NumberPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JasonWu on 7/21/2018
 */
public class TransactionFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.text_expense_total)
    TextView exTolText;
    @BindView(R.id.text_income_total)
    TextView inTolText;
    @BindView(R.id.text_cat)
    TextView textCat;
    @BindView(R.id.text_date)
    TextView textDate;
    @BindView(R.id.icon_cat)
    ImageView iconCat;

    private Long mCurWalletId;
    private BillListAdapter mAdapter;
    private BillBeanDao billBeanDao;
    private String mCurCat;
    private int mCurYear;
    private int mCurMonth;
    private List<BillBean> mAllBill;

    private boolean mIsChosen = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_transaction;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);

        mCurCat = "All";
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        mCurYear = year;
        mCurMonth = month;
        textDate.setText(CommonUtils.formatDateSimple(year, month + 1));

        billBeanDao = MyApplication.getsDaoSession().getBillBeanDao();
        mCurWalletId = getArguments() != null ? getArguments().getLong(KeyDef.WALLET_ID) : -1;
        mAdapter = new BillListAdapter(getContext());
        mAdapter.setOnItemClickListener((position, action, id) -> {
            switch (action) {
                case KeyDef.JUMP_TO_DETAIL:
                    JumpToDetail(id);
                    break;
                case KeyDef.JUMP_TO_KEEP:
                    JumpToKeep();
                    break;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = linearLayoutManager.findFirstVisibleItemPosition();
                int type = mAdapter.getItemViewType(position);
                if(type == BillListAdapter.VIEW_TYPE_BILL_TOP || type == BillListAdapter.VIEW_TYPE_BILL_NORMAL){
                    BillBean bean = mAdapter.getItemData(position);
                    String ym = CommonUtils.formatTimestamp(bean.getTime(), CommonUtils.YEAR_MONTH_PATTERN);
                    if(ym.length() > 0){
                        int year = Integer.parseInt(ym.substring(0, 4));
                        int month = Integer.parseInt(ym.substring(4, 6));
                        if(year != mCurYear || month - 1 != mCurMonth){
                            mCurYear = year;
                            mCurMonth = month - 1;
                            textDate.setText(CommonUtils.formatDateSimple(mCurYear, month));
                            updateAmountText(mAllBill);
                        }
                    }
                }
            }
        });
    }

    private void updateAmountText(List<BillBean> beans){
        Log.i(getTAG(), "" + beans.size());
        long startTime = CommonUtils.transformStartMonthToMillis(mCurYear, mCurMonth);
        long endTime = CommonUtils.transformEndMonthToMillis(mCurYear, mCurMonth);
        float exTotal = 0f;
        float inTotal = 0f;
        for (BillBean bean : beans) {
            if (bean.getTime() >= startTime && bean.getTime() <= endTime) {
                if (bean.getIsExpense()) {
                    exTotal += bean.getPrice();
                } else {
                    inTotal += bean.getPrice();
                }
            }
        }
        exTolText.setText("$" + CommonUtils.formatNumberWithComma(exTotal));
        inTolText.setText("$" + CommonUtils.formatNumberWithComma(inTotal));
    }

    @Override
    protected void updateData() {
        WhereCondition condition1 = BillBeanDao.Properties.WalletId.eq(mCurWalletId);
        WhereCondition condition2 = BillBeanDao.Properties.Category.eq(mCurCat);
        long startTime = CommonUtils.transformStartMonthToMillis(mCurYear, mCurMonth);
        long endTime = CommonUtils.transformEndMonthToMillis(mCurYear, mCurMonth);
        WhereCondition condition3 = BillBeanDao.Properties.Time.between(startTime, endTime);

        List<BillBean> beans;
        if(mIsChosen){
            if(mCurCat.equals("All")){
                beans = billBeanDao.queryBuilder().where(condition1, condition3).list();
            }else {
                beans = billBeanDao.queryBuilder().where(condition1, condition2, condition3).list();
            }
        }else {
            beans = billBeanDao.queryBuilder().where(condition1).list();
            mAllBill = beans;
        }

        updateAmountText(beans);

        if (beans.size() >= 2) {
            Collections.sort(beans, (b1, b2) -> Long.compare(b2.getTime(), b1.getTime()));
        }

        mAdapter.setData(beans, mIsChosen);
    }

    private void JumpToDetail(long billId) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(KeyDef.WALLET_ID, mCurWalletId);
        intent.putExtra(KeyDef.BILL_ID, billId);
        startActivity(intent);
    }

    private void JumpToKeep() {
        Intent intent = new Intent(getContext(), KeepActivity.class);
        intent.putExtra(KeyDef.WALLET_ID, mCurWalletId);
        startActivity(intent);
    }

    @OnClick({R.id.btn_fab, R.id.btn_choose_cat, R.id.btn_choose_date})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_fab:
                JumpToKeep();
                break;
            case R.id.btn_choose_cat:
                showPopup(v);
                break;
            case R.id.btn_choose_date:
                showDateDialog();
                break;
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.bill_cat, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            String cat = item.getTitle().toString();
            if(!mCurCat.equals(cat) || !mIsChosen){
                mIsChosen = true;
                mCurCat = cat;
                textCat.setText(mCurCat);
                iconCat.setImageResource(ResDef.CATEGORY_ICONS[item.getOrder()]);
                updateData();
            }
            return true;
        });
        popup.show();
    }

    public void showDateDialog() {
        View view = View.inflate(getContext(), R.layout.dialog_calendar, null);
        ImageView btnClose = view.findViewById(R.id.btn_close);
        TextView btnOk = view.findViewById(R.id.btn_ok);
        NumberPicker monthPicker = view.findViewById(R.id.pick_month);
        NumberPicker yearPicker = view.findViewById(R.id.pick_year);
        monthPicker.setValue(mCurMonth + 1);
        yearPicker.setValue(mCurYear);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();
        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
        });
        btnOk.setOnClickListener(v -> {
            if (mCurYear != yearPicker.getValue() || mCurMonth != monthPicker.getValue() - 1 || !mIsChosen) {
                mIsChosen = true;
                mCurYear = yearPicker.getValue();
                mCurMonth = monthPicker.getValue() - 1;
                textDate.setText(CommonUtils.formatDateSimple(mCurYear, mCurMonth + 1));
                updateData();
            }
            dialog.dismiss();
        });
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = DensityUtil.dp2px(getContext(), 270);
        dialog.getWindow().setAttributes(params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateData(UpdateBillListEvent event) {
        mCurWalletId = event.getWalletId();
        updateData();
    }
}

