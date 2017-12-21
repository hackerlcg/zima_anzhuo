package com.beihui.market.entity;


public class UserProfileAbstract {
    private String id;
    private String userName;
    private String msgIsRead;
    private String headPortrait;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsgIsRead() {
        return msgIsRead;
    }

    public void setMsgIsRead(String msgIsRead) {
        this.msgIsRead = msgIsRead;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }
}
