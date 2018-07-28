package com.rightteam.accountbook.event;

/**
 * Created by wuzhiqiang on 2018/7/25
 */
public class UpdateBillListEvent {
    private long walletId;

    public UpdateBillListEvent(long walletId){
        this.walletId = walletId;
    }

    public long getWalletId() {
        return walletId;
    }
}
