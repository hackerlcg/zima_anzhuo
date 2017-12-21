package com.beihui.market.api;


import com.beihui.market.BuildConfig;

public class NetConstants {

    public static final String DOMAIN = BuildConfig.DOMAIN;

    public static final String BASE_PATH = "/s1";

    public static final String PRODUCT_PATH = "/s3";

    public static final String SECRET_KEY = "0bca3e8e2baa42218040c5dbf6978f315e104e5c";

    public static final String ACCESS_KEY = "699b9305418757ef9a26e5a32ca9dbfb";

    /**********H5 static field********/
    public static final String H5_DOMAIN = BuildConfig.H5_DOMAIN;

    public static final String H5_HELPER = H5_DOMAIN + BuildConfig.PATH_HELPER_CENTER + "?isApp=1";

    public static final String H5_TEST = H5_DOMAIN + "/test.html?isApp=1";

    public static final String H5_NEWS_DETAIL = H5_DOMAIN + "/newsDetail.html";

    public static final String H5_LOAN_DETAIL = H5_DOMAIN + "/productDetail.html";

    public static final String H5_INVITATION = H5_DOMAIN + "/regist_h5.html";

    public static final String H5_ABOUT_US = H5_DOMAIN + BuildConfig.PATH_ABOUT_US + "?isApp=1";

    public static final String H5_USER_AGREEMENT = H5_DOMAIN + BuildConfig.PATH_USER_AGREEMENT + "?isApp=1";

    public static final String H5_INTERNAL_MESSAGE = H5_DOMAIN + "/letterDetail.html";

    public static String generateNewsUrl(String id) {
        return H5_NEWS_DETAIL + "?id=" + id + "&isApp=1";
    }

    public static String generateProductUrl(String id) {
        return H5_LOAN_DETAIL + "?id=" + id + "&isApp=1";
    }

    public static String generateInvitationUrl(String userId) {
        return H5_INVITATION + "?id=" + userId;
    }

    public static String generateInternalMessageUrl(String id) {
        return H5_INTERNAL_MESSAGE + "?id=" + id;
    }

    public static String generateTestUrl(String id) {
        return H5_TEST + "?isApp=1&id=" + id;
    }
}
