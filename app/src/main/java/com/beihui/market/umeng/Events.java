package com.beihui.market.umeng;


public class Events {
    /**
     * 进入登录界面
     */
    public static final String ENTER_LOGIN = "login_enter";
    /**
     * 登录界面-登录
     */
    public static final String LOGIN_LOGIN = "login_login";
    /**
     * 登录界面-注册
     */
    public static final String LOGIN_REGISTER = "login_register";
    /**
     * 登录界面-取消
     */
    public static final String LOGIN_CANCEL = "login_cancel";
    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "login_success";
    /**
     * 登录失败
     */
    public static final String LOGIN_FAILED = "login_failed";

    /**
     * 进入注册页面
     */
    public static final String ENTER_REGISTER = "register_enter";
    /**
     * 注册页面获取验证码
     */
    public static final String REGISTER_GET_VERIFY = "register_get_verification";
    /**
     * 注册页面获取验证码成功
     */
    public static final String REGISTER_GET_VERIFY_SUCCESS = "register_get_verification_success";
    /**
     * 注册页面获取验证码失败
     */
    public static final String REGISTER_GET_VERIFY_FAILED = "register_get_verification_failed";
    /**
     * 注册页面进入下一步
     */
    public static final String REGISTER_NEXT_STEP = "register_next_step";
    /**
     * 注册验证码成功
     */
    public static final String REGISTER_VERIFICATION_SUCCESS = "register_verification_success";
    /**
     * 注册验证码失败
     */
    public static final String REGISTER_VERIFICATION_FAILED = "register_verification_failed";
    /**
     * 注册成功
     */
    public static final String REGISTER_SUCCESS = "register_success";
    /**
     * 注册失败
     */
    public static final String REGISTER_FAILED = "register_failed";


    /**
     * 进入首页Tab
     */
    public static final String ENTER_HOME_PAGE = "home_enter";
    /**
     * 首页点击消息中心
     */
    public static final String HOME_CLICK_MESSAGE = "home_click_message";
    /**
     * 首页点击给我推荐
     */
    public static final String HOME_CLICK_RECOMMEND = "home_click_recommend";
    /**
     * 首页点击测身价
     */
    public static final String HOME_CLICK_TEST = "home_click_test";
    /**
     * 首页点击查看更多
     */
    public static final String HOME_CLICK_VIEW_MORE = "home_click_view_more";


    /**
     * 进入产品列表Tab
     */
    public static final String ENTER_LOAN_PAGE = "loan_enter";
    /**
     * 产品列表Tab-借款金额
     */
    public static final String LOAN_CLICK_AMOUNT_FILTER = "loan_click_amount_filter";
    /**
     * 产品列表Tab-借款时间
     */
    public static final String LOAN_CLICK_TIME_FILTER = "loan_click_time_filter";
    /**
     * 产品列表Tab-职业身份
     */
    public static final String LOAN_CLICK_PRO_FILTER = "loan_click_pro_filter";
    /**
     * 进入产品详情页
     */
    public static final String ENTER_LOAN_DETAIL_PAGE = "loanDetail_enter";
    /**
     * 产品详情页-分享
     */
    public static final String LOAN_DETAIL_CLICK_SHARE = "loanDetail_click_share";
    /**
     * 产品详情页-我要借款
     */
    public static final String LOAN_DETAIL_CLICK_LOAN = "loanDetail_click_loan";

    /**
     * 进入资讯Tab
     */
    public static final String ENTER_NEWS_PAGE = "news_enter";
    /**
     * 进入资讯详情页
     */
    public static final String ENTER_NEWS_DETAIL = "newsDetail_enter";
    /**
     * 资讯详情页-分享
     */
    public static final String NEWS_DETAIL_SHARE = "newsDetail_click_share";


    /**
     * 进入我的Tab
     */
    public static final String ENTER_MINE_PAGE = "mine_enter";
    /**
     * 我的Tab-消息
     */
    public static final String MINE_CLICK_MESSAGE = "mine_click_message";
    /**
     * 我的Tab-邀请好友
     */
    public static final String MINE_CLICK_INVITATION = "mine_click_invitation";
    /**
     * 我的Tab-帮助反馈
     */
    public static final String MINE_CLICK_HELP_FEEDBACK = "mine_click_help_feedback";
    /**
     * 我的Tab-设置
     */
    public static final String MINE_CLICK_SETTING = "mine_click_setting";

    /**
     * 邀请好友-邀请
     */
    public static final String INVITATION_INVITE = "invitation_invite";

    /**
     * 设置-修改密码
     */
    public static final String SETTING_CHANGE_PASSWORD = "setting_change_password";
    /**
     * 修改密码-确认
     */
    public static final String CHANGE_PASSWORD_CONFIRM = "changePassword_confirm";

    /**
     * 设置-安全退出
     */
    public static final String SETTING_EXIT = "setting_exit";
    /**
     * 安全退出-确认
     */
    public static final String EXIT_CONFIRM = "exit_confirm";
    /**
     * 安全退出-取消
     */
    public static final String EXIT_DISMISS = "exit_dismiss";
}
