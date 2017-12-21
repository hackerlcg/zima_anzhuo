package com.beihui.market.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class HotNews implements Parcelable {

    private String id;
    private String title;
    private String fileName;
    private String filePath;
    private String explain;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.fileName);
        dest.writeString(this.filePath);
        dest.writeString(this.explain);
    }

    public HotNews() {
    }

    protected HotNews(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.explain = in.readString();
    }

    public static final Parcelable.Creator<HotNews> CREATOR = new Parcelable.Creator<HotNews>() {
        @Override
        public HotNews createFromParcel(Parcel source) {
            return new HotNews(source);
        }

        @Override
        public HotNews[] newArray(int size) {
            return new HotNews[size];
        }
    };
}
