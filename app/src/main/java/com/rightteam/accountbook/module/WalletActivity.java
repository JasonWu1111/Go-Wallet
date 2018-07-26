package com.rightteam.accountbook.module;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.rightteam.accountbook.MyApplication;
import com.rightteam.accountbook.R;
import com.rightteam.accountbook.base.BaseActivity;
import com.rightteam.accountbook.bean.WalletBean;
import com.rightteam.accountbook.event.UpdateWalletListEvent;
import com.rightteam.accountbook.greendao.WalletBeanDao;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletActivity extends BaseActivity {
    @BindView(R.id.image_wallet1)
    ImageView imageWallet1;
    @BindView(R.id.image_wallet2)
    ImageView imageWallet2;
    @BindView(R.id.image_wallet3)
    ImageView imageWallet3;
    @BindView(R.id.edit_name)
    EditText editName;

    private int mCurCoverType = 0;
    private List<ImageView> covers = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void initViews() {
        covers.add(imageWallet1);
        covers.add(imageWallet2);
        covers.add(imageWallet3);
    }

    @Override
    protected void updateData() {

    }

    private void changeCoverState(int type){
        for(int i = 0; i < covers.size(); i++){
            if(i == type){
                covers.get(i).setBackgroundResource(R.drawable.frame_matrix_orange);
            }else {
                covers.get(i).setBackground(null);
            }
        }
    }

    @OnClick({R.id.btn_back, R.id.image_wallet1, R.id.image_wallet2, R.id.image_wallet3, R.id.btn_cancel, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
            case R.id.btn_back:
                finish();
                break;
            case R.id.image_wallet1:
            case R.id.image_wallet2:
            case R.id.image_wallet3:
                mCurCoverType = Integer.parseInt(view.getTag().toString());
                changeCoverState(mCurCoverType);
                break;
            case R.id.btn_ok:
                WalletBeanDao walletBeanDao = MyApplication.getsDaoSession().getWalletBeanDao();
                WalletBean bean = new WalletBean(null, mCurCoverType, editName.getEditableText().toString(), System.currentTimeMillis());
                walletBeanDao.insert(bean);
                EventBus.getDefault().post(new UpdateWalletListEvent());
                finish();
                break;
        }
    }
}
