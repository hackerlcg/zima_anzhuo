package com.beihui.market.entity;


public class Message {

    private String id;
    private String userId;
    private String title;
    private String explain;
    private int targetUserSum;
    private int readSum;
    private String image;
    private String url;
    private long gmtCreate;
    private String imgUrl;
    private int httpType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getTargetUserSum() {
        return targetUserSum;
    }

    public void setTargetUserSum(int targetUserSum) {
        this.targetUserSum = targetUserSum;
    }

    public int getReadSum() {
        return readSum;
    }

    public void setReadSum(int readSum) {
        this.readSum = readSum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getHttpType() {
        return httpType;
    }

    public void setHttpType(int httpType) {
        this.httpType = httpType;
    }
}
