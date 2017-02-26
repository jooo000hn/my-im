package com.goku.user;

/**
 * Created by user on 15/11/3.
 */
public class GkUserConst {
    /**
     * 成功状态
     */
    public static final int SUCCESS_CODE = 0;

    /**
     * 参数错误
     */
    public static final int ERROR_PARAMS_CODE = -100;
    /**
     * 数据为空错误
     */
    public static final int ERROR_BLANK_CODE = -101;
    /**
     * 更新不到数据错误
     */
    public static final int ERROR_UPDATE_CODE = -102;

    /**
     * 插入数据错误
     */
    public static final int ERROR_INSERT_CODE = -103;

    /**
     * 数据已存在
     */
    public static final int ERROR_EX_DATA_CODE = -104;

    /**
     * 用户旧密码错误
     */
    public static final int ERROR_OLD_PASSWORD_CODE = -105;

    /**
     * 登录密码错误
     */
    public static final int ERROR_PASSWORD_CODE = -106;

    /**
     * 登录账户不存在
     */
    public static final int ERROR_ACCOUNT_CODE = -107;

    /**
     * 登录账户不存在需要绑定手机号
     */
    public static final int ERROR_ACCOUNT_BIND_CODE = -108;

    /**
     * ip尝试登录次数超过限制
     */
    public static final int ERROR_LOGINIP_BANTIMES_CODE = -109;

    /**
     * 登录错误次数超过限制，账号的冻结中
     */
    public static final int ERROR_ACCOUNT_INTERVAL_CODE = -110;

    /**
     * 账户登陆成功次数超过限制
     */
    public static final int ERROR_ACCOUNT_LOGIN_CODE = -111;

    /**
     * 用户类型：平台
     */
    public static final String USER_TYPE_PLATFROM="0";
    /**
     * 用户类型：ACFUN
     */
    public static final String USER_TYPE_ACFUN="1";

    /**
     * 用户类型：微信
     */
    public static final String USER_TYPE_WEIXIN="2";
    /**
     * 用户类型：后台
     */
    public static final String USER_TYPE_BACKPLATFROM="3";
    /**
     * 游戏平台
     */
    public static final String CREATE_BY_PLATFROM_GAME="game";

    /**
     * IM平台
     */
    public static final String CREATE_BY_PLATFROM_IM="im";

    /**
     * IM后台
     */
    public static final String CREATE_BY_PLATFROM_IMBACK="imback";

    /**
     * 少儿平台
     */
    public static final String CREATE_BY_PLATFROM_WULATOWN="wulatown";

    //登录token Map的key
    public static final String LOGIN_MAP_USERID="userID";
    public static final String LOGIN_MAP_TOKEN="token";
    public static final String LOGIN_MAP_CODE="code";
    public static final String MAP_USERID="userID";

    //微信用户 默认平台密码
    public static final String WX_USER_PASSWORD="000000";

}
