package com.beihui.market.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class LoanProduct {

    private int total;
    private List<Row> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public static class Row implements Parcelable {
        /**
         * 是否是快捷推荐 0不是，1是
         */
        private int quickCommend;
        /**
         * 最低利率
         */
        private String interestLowText;
        /**
         * 期限范围
         */
        private String dueTimeText;
        private String logoUrl;
        /**
         * 成功借款人数
         */
        private int successCount;
        private String id;
        /**
         * 产品标签， 0--无 1--最新 2--热门 3--强力推荐 4--精选
         */
        private int productSign;
        private String productName;
        /**
         * tags,spilt by ,
         */
        private String feature;
        /**
         * 借款额度
         */
        private String borrowingHighText;
        /**
         * 借款利息类型，日，月
         */
        private String interestTimeTypeText;

        private String interestTimeText;

        public int getQuickCommend() {
            return quickCommend;
        }

        public void setQuickCommend(int quickCommend) {
            this.quickCommend = quickCommend;
        }

        public String getInterestLowText() {
            return interestLowText;
        }

        public void setInterestLowText(String interestLowText) {
            this.interestLowText = interestLowText;
        }

        public String getDueTimeText() {
            return dueTimeText;
        }

        public void setDueTimeText(String dueTimeText) {
            this.dueTimeText = dueTimeText;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public void setSuccessCount(int successCount) {
            this.successCount = successCount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getProductSign() {
            return productSign;
        }

        public void setProductSign(int productSign) {
            this.productSign = productSign;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getBorrowingHighText() {
            return borrowingHighText;
        }

        public void setBorrowingHighText(String borrowingHighText) {
            this.borrowingHighText = borrowingHighText;
        }

        public String getInterestTimeTypeText() {
            return interestTimeTypeText;
        }

        public void setInterestTimeTypeText(String interestTimeTypeText) {
            this.interestTimeTypeText = interestTimeTypeText;
        }

        public String getInterestTimeText() {
            return interestTimeText;
        }

        public void setInterestTimeText(String interestTimeText) {
            this.interestTimeText = interestTimeText;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.quickCommend);
            dest.writeString(this.interestLowText);
            dest.writeString(this.dueTimeText);
            dest.writeString(this.logoUrl);
            dest.writeInt(this.successCount);
            dest.writeString(this.id);
            dest.writeInt(this.productSign);
            dest.writeString(this.productName);
            dest.writeString(this.feature);
            dest.writeString(this.borrowingHighText);
            dest.writeString(this.interestTimeTypeText);
        }

        public Row() {
        }

        protected Row(Parcel in) {
            this.quickCommend = in.readInt();
            this.interestLowText = in.readString();
            this.dueTimeText = in.readString();
            this.logoUrl = in.readString();
            this.successCount = in.readInt();
            this.id = in.readString();
            this.productSign = in.readInt();
            this.productName = in.readString();
            this.feature = in.readString();
            this.borrowingHighText = in.readString();
            this.interestTimeTypeText = in.readString();
        }

        public static final Parcelable.Creator<Row> CREATOR = new Parcelable.Creator<Row>() {
            @Override
            public Row createFromParcel(Parcel source) {
                return new Row(source);
            }

            @Override
            public Row[] newArray(int size) {
                return new Row[size];
            }
        };
    }
}
