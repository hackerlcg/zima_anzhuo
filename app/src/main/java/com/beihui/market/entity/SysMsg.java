package com.beihui.market.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SysMsg {

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
        private String explain;
        private String id;
        private String title;
        private long gmtCreate;
        private int isRead;

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.explain);
            dest.writeString(this.id);
            dest.writeString(this.title);
            dest.writeLong(this.gmtCreate);
            dest.writeInt(this.isRead);
        }

        public Row() {
        }

        protected Row(Parcel in) {
            this.explain = in.readString();
            this.id = in.readString();
            this.title = in.readString();
            this.gmtCreate = in.readLong();
            this.isRead = in.readInt();
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
