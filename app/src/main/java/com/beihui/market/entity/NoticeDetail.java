package com.beihui.market.entity;


public class NoticeDetail {

    private String explain;
    private int readSum;
    private long gmtCreate;
    private String title;
    private String content;

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getReadSum() {
        return readSum;
    }

    public void setReadSum(int readSum) {
        this.readSum = readSum;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
