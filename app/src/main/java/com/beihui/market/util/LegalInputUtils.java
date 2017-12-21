package com.beihui.market.util;


import android.text.TextUtils;

public class LegalInputUtils {

    /**
     * 输入的手机号和密码是否符合规范
     *
     * @param phone    手机号
     * @param password 密码
     */
    public static boolean isPhoneAndPwdLegal(String phone, String password) {
        return validatePhone(phone) && validatePassword(password);
    }

    public static boolean validatePassword(String str) {
        if (TextUtils.isEmpty(str) || str.length() < 6 || str.length() > 16) {
            return false;
        }
//        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        String regex = "^[0-9A-Za-z]{6,16}$";
        return str.matches(regex);
    }


    public static boolean validatePhone(String phone) {
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            return false;
        }
        /**
         * 手机号码:
         * 13[0-9], 14[5,7], 15[0, 1, 2, 3, 5, 6, 7, 8, 9], 17[6, 7, 8], 18[0-9], 170[0-9]
         * 移动号段: 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
         * 联通号段: 130,131,132,155,156,185,186,145,176,1709
         * 电信号段: 133,153,180,181,189,177,1700
         */
        String mobile = "^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$";
        /**
         * 中国移动：China Mobile
         * 134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
         * "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";
         */
        String cm = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";
        /**
         * 中国联通：China Unicom
         * 130,131,132,155,156,185,186,145,176,1709
         * "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)"
         */
        String cu = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";
        /**
         * 中国电信：China Telecom
         * 133,153,180,181,189,177,1700
         * "(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)"
         */
        String ct = "(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)";

        return phone.matches(mobile) || phone.matches(cm) || phone.matches(cu) || phone.matches(ct);
    }
}
