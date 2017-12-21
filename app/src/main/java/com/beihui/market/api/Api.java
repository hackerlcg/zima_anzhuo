package com.beihui.market.api;


import android.util.Base64;

import com.beihui.market.App;
import com.beihui.market.BuildConfig;
import com.beihui.market.api.interceptor.AccessHeadInterceptor;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.AppUpdate;
import com.beihui.market.entity.Avatar;
import com.beihui.market.entity.HotNews;
import com.beihui.market.entity.Invitation;
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.entity.LoanProductDetail;
import com.beihui.market.entity.Message;
import com.beihui.market.entity.News;
import com.beihui.market.entity.Notice;
import com.beihui.market.entity.NoticeAbstract;
import com.beihui.market.entity.NoticeDetail;
import com.beihui.market.entity.Phone;
import com.beihui.market.entity.Profession;
import com.beihui.market.entity.SysMsg;
import com.beihui.market.entity.SysMsgAbstract;
import com.beihui.market.entity.SysMsgDetail;
import com.beihui.market.entity.UserProfile;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.entity.request.RequestConstants;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Api {

    private static Api sInstance;
    private ApiService service;

    public static Api getInstance() {
        if (sInstance == null) {
            synchronized (Api.class) {
                if (sInstance == null) {
                    sInstance = new Api(createHttpClient());
                }
            }
        }
        return sInstance;
    }

    private static OkHttpClient createHttpClient() {
        File cacheFile = new File(App.getInstance().getCacheDir().getAbsolutePath(), "MarketCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(cache)
                .addInterceptor(new AccessHeadInterceptor());


        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        return builder.build();
    }

    private Api(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConstants.DOMAIN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(ApiService.class);
    }

    /**
     * 登录
     *
     * @param account 用户账号
     * @param pwd     用户密码
     */
    public Observable<ResultEntity<UserProfileAbstract>> login(String account, String pwd) {
        return service.login(account, generatePwd(pwd, account));
    }

    /**
     * 请求注册验证码
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestRegisterSms(String phone) {
        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_REGISTER);
    }

    /**
     * 请求重置密码验证码
     *
     * @param phone 请求手机号
     */
    public Observable<ResultEntity<Phone>> requestRestPwdSms(String phone) {
        return service.requestSms(phone, RequestConstants.VERIFICATION_TYPE_RESET_PWD);
    }

    /**
     * 验证注册验证码
     *
     * @param phone 请求的手机号
     * @param code  请求的验证码
     */
    public Observable<ResultEntity> verifyRegisterCode(String phone, String code) {
        return service.verifyCode(phone, code, RequestConstants.VERIFICATION_TYPE_REGISTER);
    }

    /**
     * 验证重置密码验证码
     *
     * @param phone 请求的手机号
     * @param code  请求的验证码
     */
    public Observable<ResultEntity> verifyResetPwdCode(String phone, String code) {
        return service.verifyCode(phone, code, RequestConstants.VERIFICATION_TYPE_RESET_PWD);
    }

    /**
     * 注册
     *
     * @param phone      注册手机号
     * @param pwd        用户密码
     * @param channelId  app渠道
     * @param inviteCode 邀请码，可控
     */
    public Observable<ResultEntity> register(String phone, String pwd, String channelId, String inviteCode) {
        return service.register(RequestConstants.PLATFORM, phone, generatePwd(pwd, phone), channelId, inviteCode);
    }

    /**
     * 重置密码
     *
     * @param account 用户手机号
     * @param pwd     新密码
     */
    public Observable<ResultEntity> resetPwd(String account, String pwd) {
        return service.updatePwd(null, account, RequestConstants.UPDATE_PWD_TYPE_RESET, generatePwd(pwd, account), null, null);
    }

    /**
     * 修改密码
     *
     * @param account   用户手机号
     * @param pwd       新密码
     * @param originPwd 原密码
     */
    public Observable<ResultEntity> updatePwd(String id, String account, String pwd, String originPwd) {
        String generatedPwd = generatePwd(pwd, account);
        return service.updatePwd(id, account, RequestConstants.UPDATE_PWD_TYPE_CHANGE,
                generatedPwd, generatePwd(originPwd, account), generatedPwd);
    }

    /**
     * 查询用户个人信息
     *
     * @param id 用户id
     */
    public Observable<ResultEntity<UserProfile>> queryUserProfile(String id) {
        return service.userProfile(id);
    }

    /**
     * 更新用户头像
     *
     * @param userId   用户id
     * @param fileName 文件名
     * @param avatar   头像
     */
    public Observable<ResultEntity<Avatar>> updateUserAvatar(String userId, String fileName, byte[] avatar) {
        String avatarBase64 = Base64.encodeToString(avatar, Base64.DEFAULT);
        return service.updateUserAvatar(userId, fileName, avatarBase64);
    }

    /**
     * 修改用户名
     *
     * @param id       用户id
     * @param username 新用户名
     */
    public Observable<ResultEntity> updateUsername(String id, String username) {
        return service.updateUsername(id, username);
    }

    /**
     * 获取职业列表
     *
     * @param id 用户id
     */
    public Observable<ResultEntity<ArrayList<Profession>>> queryProfession(String id) {
        return service.queryProfession(id);
    }

    /**
     * 更新用户职业
     *
     * @param id             用户id
     * @param professionType 职业类型
     */
    public Observable<ResultEntity> updateUserProfession(String id, int professionType) {
        return service.updateProfession(id, professionType);
    }

    /**
     * 退出登录
     *
     * @param id 用户id
     */
    public Observable<ResultEntity> logout(String id) {
        return service.logout(id);
    }


    /**
     * 消息中心-公告
     */
    public Observable<ResultEntity<NoticeAbstract>> queryNoticeHome() {
        return service.noticeHome();
    }

    /**
     * 查询公告列表
     *
     * @param pageNum  查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<Notice>> queryNoticeList(int pageNum, int pageSize) {
        return service.noticeList(pageNum, pageSize);
    }

    /**
     * 查询公告详情
     *
     * @param id 公告id
     */
    public Observable<ResultEntity<NoticeDetail>> queryNoticeDetail(String id) {
        return service.noticeDetail(id);
    }

    /**
     * 消息中心-系统消息
     *
     * @param userId 用户id
     */
    public Observable<ResultEntity<SysMsgAbstract>> querySysMsgHome(String userId) {
        return service.sysMsgHome(userId);
    }

    /**
     * 查询系统消息列表
     *
     * @param userId   用户id
     * @param pageNum  查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<SysMsg>> querySysMsgList(String userId, int pageNum, int pageSize) {
        return service.sysMsgList(userId, pageNum, pageSize);
    }

    /**
     * 查询系统消息详情
     *
     * @param id 消息id
     */
    public Observable<ResultEntity<SysMsgDetail>> querySysMsgDetail(String id) {
        return service.sysMsgDetail(id);
    }

    /**
     * 资讯列表
     *
     * @param pageNum  查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<News>> queryNews(int pageNum, int pageSize) {
        return service.queryNews(pageNum, pageSize);
    }

    /**
     * 站内信
     */
    public Observable<ResultEntity<List<Message>>> queryMessage(int pageNum, int pageSize) {
        return service.queryMessages(pageNum, pageSize);
    }

    /**
     * 查询启动页广告，banner，弹窗广告
     *
     * @param supernatantType 查询类型
     */
    public Observable<ResultEntity<List<AdBanner>>> querySupernatant(int supernatantType) {
        return service.querySupernatant(RequestConstants.PLATFORM, supernatantType);
    }

    /**
     * 查询头条消息
     */
    public Observable<ResultEntity<List<String>>> queryBorrowingScroll() {
        return service.queryBorrowingScroll();
    }

    /**
     * 查询首页热门资讯
     */
    public Observable<ResultEntity<List<HotNews>>> queryHotNews() {
        return service.queryHotNews();
    }

    /**
     * 查询首页热门贷款产品
     */
    public Observable<ResultEntity<List<LoanProduct.Row>>> queryHotLoanProducts() {
        return service.queryHotLoanProducts();
    }

    /**
     * 条件查询贷款产品列表
     *
     * @param amount   目标金额
     * @param dueTime  借款期限
     * @param pro      职业身份
     * @param pageNum  查询页数
     * @param pageSize 查询每页大小
     */
    public Observable<ResultEntity<LoanProduct>> queryLoanProduct(double amount, int dueTime, int pro, int pageNum, int pageSize) {
        return service.queryLoanProduct(amount, dueTime + "", pro + "", pageNum, pageSize);
    }

    /**
     * 查询贷款产品详情
     *
     * @param id     产品id
     * @param userId 用户id
     */
    public Observable<ResultEntity<LoanProductDetail>> queryLoanProductDetail(String id, String userId) {
        return service.queryLoanProductDetail(id, userId);
    }

    /**
     * 查询邀请详细
     *
     * @param userId 用户id
     */
    public Observable<ResultEntity<Invitation>> queryInvitation(String userId) {
        return service.queryInvitation(userId);
    }

    /**
     * 查询版本更新
     */
    public Observable<ResultEntity<AppUpdate>> queryUpdate() {
        return service.queryAppUpdate(RequestConstants.PLATFORM + "");
    }

    /**
     * 提交用户反馈
     *
     * @param userId  用户id
     * @param content 反馈内容
     */
    public Observable<ResultEntity> submitFeedback(String userId, String content) {
        return service.submitFeedback(userId, content);
    }

    /*********数据统计********/

    /**
     * 点击第三方产品外链
     *
     * @param userId 用户Id
     * @param id     产品id
     */
    public Observable<ResultEntity> onProductClicked(String userId, String id) {
        return service.onProductClicked(userId, id);
    }

    /**
     * 点击广告，包括启动页，弹窗，banner
     *
     * @param id     广告id
     * @param userId 用户id，可为空
     * @param type   广告类型
     */
    public Observable<ResultEntity> onAdClicked(String id, String userId, int type) {
        return service.onAdClicked(id, userId, type);
    }

    /**
     * 统计站内信点击次数
     *
     * @param id 站内信id
     */
    public Observable<ResultEntity> onInternalMessageClicked(String id) {
        return service.onInternalMessageClicked(id);
    }
    /*****************/

    /*****generate method*****/
    //加密密码
    private String generatePwd(String pwd, String account) {
        String sha = new String(Hex.encodeHex(DigestUtils.sha512(pwd)));
        String md5 = new String(Hex.encodeHex(DigestUtils.md5(sha + account)));
        return md5.toUpperCase();
    }
}
