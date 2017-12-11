package com.lzq.adream.model;

/**
 * Created by Administrator on 2017/8/25.
 */

public class NotificationItem {
    private  String remark_;
    private  String category_;
    private String orderId_;
    private String amount_;
    private String status;

    public String getRemark_() {
        return remark_;
    }

    public void setRemark_(String remark_) {
        this.remark_ = remark_;
    }

    public String getCategory_() {
        return category_;
    }

    public void setCategory_(String category_) {
        this.category_ = category_;
    }

    public String getOrderId_() {
        return orderId_;
    }

    public void setOrderId_(String orderId_) {
        this.orderId_ = orderId_;
    }

    public String getAmount_() {
        return amount_;
    }

    public void setAmount_(String amount_) {
        this.amount_ = amount_;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
