package com.rightteam.accountbook.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.adapter.BillPerAdapter;
import com.rightteam.accountbook.base.BaseFragment;
import com.rightteam.accountbook.bean.BillBean;
import com.rightteam.accountbook.bean.BillPerBean;
import com.rightteam.accountbook.constants.KeyDef;
import com.rightteam.accountbook.constants.ResDef;
import com.rightteam.accountbook.event.UpdateBillListEvent;
import com.rightteam.accountbook.greendao.BillBeanDao;
import com.rightteam.accountbook.view.RingView;
import com.rightteam.accountbook.view.RoundView;
import com.rightteam.accountbook.utils.CommonUtils;
import com.rightteam.accountbook.utils.DensityUtil;
import com.shawnlin.numberpicker.NumberPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JasonWu on 7/31/2018
 */
public class StatementsFragment extends BaseFragment {
    @BindView(R.id.ring_view)
    RingView ringView;
    @BindView(R.id.text_date)
    TextView textDate;
    @BindView(R.id.frame_stat)
    FrameLayout frameStat;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.text_source)
    TextView textSource;
    @BindView(R.id.text_price)
    TextView textPrice;
    @BindView(R.id.btn_expense)
    TextView btnExpense;
    @BindView(R.id.btn_income)
    TextView btnIncome;


    private Long mCurWalletId;
    private int mCurYear;
    private int mCurMonth;
    private boolean mIsExpense = true;
    private BillBeanDao billBeanDao;
    private BillPerAdapter mAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_statements;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        mCurYear = year;
        mCurMonth = month;

        billBeanDao = MyApplication.getsDaoSession().getBillBeanDao();
        textDate.setText(CommonUtils.formatDateSimple(year, month + 1));
        mCurWalletId = getArguments() != null ? getArguments().getLong(KeyDef.WALLET_ID) : -1;

        mAdapter = new BillPerAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void updateData() {
        frameStat.removeAllViews();

        textSource.setText(mIsExpense ? "Expense" : "Income");
        WhereCondition condition1 = BillBeanDao.Properties.WalletId.eq(mCurWalletId);
        WhereCondition condition2 = BillBeanDao.Properties.IsExpense.eq(mIsExpense);

        long startTime = CommonUtils.transformStartMonthToMillis(mCurYear, mCurMonth);
        long endTime = CommonUtils.transformEndMonthToMillis(mCurYear, mCurMonth);
        WhereCondition condition3 = BillBeanDao.Properties.Time.between(startTime, endTime);

        List<BillBean> beans = billBeanDao.queryBuilder().where(condition1, condition2, condition3).list();
        List<BillPerBean> perBeans = new ArrayList<>();
        float total = 0;
        @SuppressLint("UseSparseArrays") HashMap<Integer, Float> map = new HashMap<>();
        for (int i = 0; i < (mIsExpense ? 10 : 4); i++) {
            map.put(i, 0f);
        }

        for (BillBean bean : beans) {
            total += bean.getPrice();
            float amount = map.get(bean.getType());
            amount += bean.getPrice();
            map.put(bean.getType(), amount);
        }

        textPrice.setText(String.format("%s%s", CommonUtils.getCurrency(), CommonUtils.formatNumberWithComma(total)));

        for (int i = 0; i < (mIsExpense ? 10 : 4); i++) {
            if (map.get(i) != 0f) {
                BillPerBean perBean = new BillPerBean();
                perBean.setExpense(mIsExpense);
                perBean.setPrice(map.get(i));
                perBean.setType(i);
                float per = Float.parseFloat(CommonUtils.formatNumberPercent(map.get(i) * 100 / total));
                perBean.setPer(per);
                perBeans.add(perBean);
            }
        }

        if (perBeans.size() >= 2) {
            Collections.sort(perBeans, (b1, b2) -> Float.compare(b2.getPer(), b1.getPer()));
        }
        mAdapter.setData(perBeans);

        ringView.setPers(perBeans);
        frameStat.post(() -> {
            double startAngle = 0;
            int ringRadius = DensityUtil.dp2px(getContext(), 93);
            int contentWidth = DensityUtil.dp2px(getContext(), 88);
            int contentHeight = DensityUtil.dp2px(getContext(), 32);
            int rv_offsetX = DensityUtil.dp2px(getContext(), 10);
            int rv_offsetY = DensityUtil.dp2px(getContext(), 10.5F);

            for (BillPerBean bean : perBeans) {
                double sweep = bean.getPer() * Math.PI * 2 / 100;
                double cos = Math.cos(-startAngle - sweep / 2);
                double sin = Math.sin(-startAngle - sweep / 2);

                boolean isRight = cos >= 0;
                boolean isUp = sin >= 0;

                int resId = isRight ? R.layout.view_bill_info_right : R.layout.view_bill_info_left;

                View view = View.inflate(getContext(), resId, null);
                frameStat.addView(view);
                TextView textPrice = view.findViewById(R.id.text_price);
                TextView textCat = view.findViewById(R.id.text_cat);
                textPrice.setText(String.format("%s%s", CommonUtils.getCurrency(), CommonUtils.formatNumberWithComma(bean.getPrice())));
                textCat.setText(mIsExpense ? ResDef.TYPE_NAMES_EX[bean.getType()] : ResDef.TYPE_NAMES_IN[bean.getType()]);

                View lingView = view.findViewById(R.id.line_straight);
                View upView = view.findViewById(R.id.line_slash_up);
                View downView = view.findViewById(R.id.line_slash_down);
                RoundView roundView = view.findViewById(R.id.view_round);
                int color = Color.parseColor(mIsExpense ? ResDef.TYPE_COLORS_EX[bean.getType()] : ResDef.TYPE_COLORS_IN[bean.getType()]);
                lingView.setBackgroundColor(color);
                upView.setBackgroundColor(color);
                downView.setBackgroundColor(color);
                roundView.setPaintColor(color);

                int offsetX = 0;
                int offsetY = 0;

                if (isUp) {
                    offsetY = -contentHeight;
                    upView.setVisibility(View.INVISIBLE);
                    roundView.setTranslationY(rv_offsetY);
                } else {
                    downView.setVisibility(View.INVISIBLE);
                    roundView.setTranslationY(-rv_offsetY);
                }

                if(isRight){
                    roundView.setTranslationX(-rv_offsetX);
                }else {
                    offsetX = -contentWidth;
                    roundView.setTranslationX(rv_offsetX);
                }

                view.setTranslationX((float) (frameStat.getWidth() / 2 + ringRadius * cos + offsetX));
                view.setTranslationY((float) (frameStat.getHeight() / 2 - ringRadius * sin + offsetY));
                startAngle += sweep;
            }
        });
    }


    @OnClick({R.id.btn_choose_date, R.id.btn_expense, R.id.btn_income})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_choose_date:
                showDateDialog();
                break;
            case R.id.btn_expense:
                if (!mIsExpense) {
                    mIsExpense = true;
                    changeBtnState();
                }
                break;
            case R.id.btn_income:
                if (mIsExpense) {
                    mIsExpense = false;
                    changeBtnState();
                }
                break;
        }

    }

    private void changeBtnState() {
        btnExpense.setTextColor(getResources().getColor(mIsExpense ? R.color.white : R.color.blue_dark));
        btnExpense.setBackgroundResource(mIsExpense ? R.drawable.frame_half_blue_left : R.drawable.frame_half_blue2_left);
        btnIncome.setTextColor(getResources().getColor(!mIsExpense ? R.color.white : R.color.blue_dark));
        btnIncome.setBackgroundResource(!mIsExpense ? R.drawable.frame_half_blue_right : R.drawable.frame_half_blue2_right);
        updateData();
    }

    private void showDateDialog() {
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
            if (mCurYear != yearPicker.getValue() || mCurMonth != monthPicker.getValue() - 1) {
                mCurYear = yearPicker.getValue();
                mCurMonth = monthPicker.getValue() - 1;
                textDate.setText(CommonUtils.formatDateSimple(mCurYear, mCurMonth + 1));
                updateData();
            }
            dialog.dismiss();
        });
        dialog.show();
        WindowManager.LayoutParams params = Objects.requireNonNull(dialog.getWindow()).getAttributes();
        params.width = DensityUtil.dp2px(Objects.requireNonNull(getContext()), 270);
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
