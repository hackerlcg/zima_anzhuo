package com.beihui.market.entity.request;


public class RequestConstants {

    /**
     * Android平台
     */
    public static final int PLATFORM = 1;

    /**
     * 更新密码-重置密码
     */
    public static final int UPDATE_PWD_TYPE_RESET = 2;
    /**
     * 更新密码-修改密码
     */
    public static final int UPDATE_PWD_TYPE_CHANGE = 3;

    /**
     * 验证码-注册
     */
    public static final String VERIFICATION_TYPE_REGISTER = "0";
    /**
     * 验证码-重置密码
     */
    public static final String VERIFICATION_TYPE_RESET_PWD = "2";

    public static final int SUP_TYPE_AD = 1;
    public static final int SUP_TYPE_BANNER = 2;
    public static final int SUP_TYPE_DIALOG = 3;

    public static final int AVATAR_BYTE_SIZE = 15 * 1024;
}
