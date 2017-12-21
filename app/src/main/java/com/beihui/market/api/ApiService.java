package com.beihui.market.api;

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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static com.beihui.market.api.NetConstants.BASE_PATH;
import static com.beihui.market.api.NetConstants.PRODUCT_PATH;

public interface ApiService {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/login")
    Observable<ResultEntity<UserProfileAbstract>> login(@Field("account") String account, @Field("pwd") String pwd);

    /**
     * 请求验证码
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/sms/sendSms")
    Observable<ResultEntity<Phone>> requestSms(@Field("phone") String phone, @Field("type") String type);

    /**
     * 验证验证码
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/verificationCodeVerify")
    Observable<ResultEntity> verifyCode(@Field("account") String account, @Field("verificationCode") String verificationCode,
                                        @Field("verificationCodeType") String verificationCodeType);

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/register")
    Observable<ResultEntity> register(@Field("platform") int platform, @Field("account") String account,
                                      @Field("pwd") String pwd, @Field("channelId") String channelId,
                                      @Field("inviteCode") String inviteCode);

    /**
     * 更新密码，重置或者修改
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/updatePwd")
    Observable<ResultEntity> updatePwd(@Field("userId") String id, @Field("account") String account, @Field("pwdType") int pwdType,
                                       @Field("pwd") String pwd, @Field("originPwd") String originPwd,
                                       @Field("pwd2") String pwd2);

    /**
     * 用户个人中心信息
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUserDetail/personalCenter")
    Observable<ResultEntity<UserProfile>> userProfile(@Field("userId") String id);


    @FormUrlEncoded
    @POST(BASE_PATH + "/attach/uploadUserHeadPortrait")
    Observable<ResultEntity<Avatar>> updateUserAvatar(@Field("userId") String userId, @Field("fileName") String fileName,
                                                      @Field("fileBase64") String fileBase64);

    /**
     * 修改用户名
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUserDetail/updateNickName")
    Observable<ResultEntity> updateUsername(@Field("userId") String id, @Field("userName") String userName);

    /**
     * 获取职业列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUserDetail/showProfession")
    Observable<ResultEntity<ArrayList<Profession>>> queryProfession(@Field("userId") String id);

    /**
     * 修改职业
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUserDetail/updateProfession")
    Observable<ResultEntity> updateProfession(@Field("userId") String id, @Field("professionType") int professionType);

    /**
     * 退出登录
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/logout")
    Observable<ResultEntity> logout(@Field("userId") String id);

    /**
     * 消息中心-公告
     */
    @POST(BASE_PATH + "/notice/home")
    Observable<ResultEntity<NoticeAbstract>> noticeHome();

    /**
     * 公告列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/notice/list")
    Observable<ResultEntity<Notice>> noticeList(@Field("pageNo") int pageNum, @Field("pageSize") int pageSize);

    /**
     * 公告详情
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/notice/details")
    Observable<ResultEntity<NoticeDetail>> noticeDetail(@Field("id") String id);

    /**
     * 消息中心-系统消息
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/systemMessage/home")
    Observable<ResultEntity<SysMsgAbstract>> sysMsgHome(@Field("userId") String userId);

    /**
     * 系统消息列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/systemMessage/list")
    Observable<ResultEntity<SysMsg>> sysMsgList(@Field("userId") String userId, @Field("pageNo") int pageNum,
                                                @Field("pageSize") int pageSize);

    /**
     * 系统消息详情
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/systemMessage/details")
    Observable<ResultEntity<SysMsgDetail>> sysMsgDetail(@Field("id") String id);

    /**
     * 资讯列表
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/information/list")
    Observable<ResultEntity<News>> queryNews(@Field("pageNo") int pageNum, @Field("pageSize") int pageSize);

    /**
     * 站内信
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/pushInfo/queryMessage")
    Observable<ResultEntity<List<Message>>> queryMessages(@Field("pageNo") int pageNum, @Field("pageSize") int pageSize);

    /**
     * 启动页广告，banner，弹窗广告
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/supernatant/querySupernatant")
    Observable<ResultEntity<List<AdBanner>>> querySupernatant(@Field("port") int port, @Field("supernatantType") int supernatantType);

    /**
     * 头条滚动信息
     */
    @POST(BASE_PATH + "/supernatant/queryBorrowingScroll")
    Observable<ResultEntity<List<String>>> queryBorrowingScroll();

    /**
     * 获取首页热门资讯
     */
    @GET(BASE_PATH + "/information/hotList")
    Observable<ResultEntity<List<HotNews>>> queryHotNews();

    /**
     * 获取首页热门贷款产品
     */
    @GET(PRODUCT_PATH + "/product/hotList")
    Observable<ResultEntity<List<LoanProduct.Row>>> queryHotLoanProducts();


    /**
     * 查询贷款产品列表
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/list")
    Observable<ResultEntity<LoanProduct>> queryLoanProduct(@Field("amount") double amount, @Field("dueTime") String dueTime,
                                                           @Field("orientCareer") String pro, @Field("pageNo") int pageNum,
                                                           @Field("pageSize") int pageSize);

    /**
     * 查询贷款产品详情
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/details")
    Observable<ResultEntity<LoanProductDetail>> queryLoanProductDetail(@Field("id") String productId, @Field("userId") String userId);

    /**
     * 查询邀请详细
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUserDetail/invite")
    Observable<ResultEntity<Invitation>> queryInvitation(@Field("userId") String userId);

    /**
     * 查询版本更新
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/version/queryVersion")
    Observable<ResultEntity<AppUpdate>> queryAppUpdate(@Field("clientType") String clientType);

    /**
     * 用户反馈
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/clientUser/insertFeedback")
    Observable<ResultEntity> submitFeedback(@Field("userId") String userId, @Field("content") String content);

    /*******数据统计********/

    /**
     * 点击第三方产品外链
     */
    @FormUrlEncoded
    @POST(PRODUCT_PATH + "/product/productSkip")
    Observable<ResultEntity> onProductClicked(@Field("userId") String userId, @Field("id") String id);

    /**
     * 点击广告，包括启动页，弹窗，banner
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/supernatant/loadSupernatant")
    Observable<ResultEntity> onAdClicked(@Field("id") String id, @Field("userId") String userId, @Field("supernatantType") int supernatantType);

    /**
     * 统计站内信点击次数
     */
    @FormUrlEncoded
    @POST(BASE_PATH + "/pushInfo/details")
    Observable<ResultEntity> onInternalMessageClicked(@Field("id") String id);

    /***************/
}
