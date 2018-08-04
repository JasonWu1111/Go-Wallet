package com.rightteam.accountbook.bean;

/**
 * Created by JasonWu on 8/4/2018
 */
public class BillPerBean {
    private int type;
    private boolean isExpense;
    private float price;
    private float per;

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(boolean expense) {
        isExpense = expense;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPer() {
        return per;
    }

    public void setPer(float per) {
        this.per = per;
    }
}
