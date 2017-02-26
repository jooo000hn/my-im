package com.goku.user.service;

import com.goku.user.model.GkUser;
import com.goku.user.model.OauthUser;

import java.util.List;
import java.util.Map;

/**
 * 接口
 * User: moueimei
 * Date: 15/10/23
 * Version: 1.0
 */
public interface GkUserService {

    /**
     * 根据根据用户平台ID查询前台用户
     * @param puId  用户平台ID
     * @return 前台用户对象
     */
    public GkUser findUserByPUId(int puId);

    /**
     * 根据根据用户平台ID集合查询前台用户信息集合
     * @param puIds  用户平台ID集合
     * @return 前台用户信息集合
     */
    public List<GkUser> findUsersByPUIds(List<Integer> puIds);

    /**
     * 根据根据用户平台ID集合查询前台用户map
     * @param puIds  用户平台ID集合
     * @return 前台用户map  key:平台id
     */
    public Map<Integer,GkUser> findUserMapByPUIds(List<Integer> puIds);

    /**
     * 根据username查询前台用户
     * @param username 前台用户名或手机号
     * @return 前台用户对象
     */
    public GkUser findUserByUserName(String username);

    /**
     * 根据username查询前台用户
     * @param phone 手机号
     * @return 前台用户对象
     */
    public GkUser findUserByPhone(String phone);

    /**
     * 根据手机号判断是否存在
     * @param phone
     * @return
     */
    public boolean existPhone(String phone);

    /**
     * 查找Oauth系统用户
     * @param oauthParamJson
     * @return Oauth系统用户
     */
    public OauthUser findOauthUserByName(String oauthParamJson);

    /**
     * 根据username和密码查询Oauth系统用户
     * @param username 前台用户名或手机号
     * @param password 前台用户密码
     * @return  前台用户
     */
    public GkUser findUserByUserameAndPassword(String username, String password);

    /**
     * 注册用户
     * @param username
     * @param password
     * @param createBy
     * @return
     */
    public int register(String username, String password, String createBy);

    /**
     * 手机注册用户
     * @param phone  手机号
     * @param password
     * @param createBy
     * @return key:"code";"userID";
     */
    public Map<String,String> registerPhone(String phone, String password, String createBy) ;

    /**
     * 编辑用户信息
     * @param User
     * @return
     */
    public int submitUserInfo(GkUser User);

    /**
     * 清除用户绑定手机号
     * @param userId  前台用户ID
     * @param phone  前台用户电话
     * @return  成功状态
     */
    public boolean canclePhone(int userId, String phone);

    /**
     * 用户绑定手机号
     * @param userId  前台用户ID
     * @param phone  前台用户电话
     * @return  成功状态
     */
    public boolean bindPhone(int userId, String phone);

    /**
     * 用户修改密码
     * @param userId  前台用户ID
     * @param newPassword  前台用户新密码
     * @param oldPassword 用户旧密码
     * @return  成功状态
     */
    public int modifyPassword(int userId, String newPassword, String oldPassword);

    /**
     * 用户修改密码
     * @param userId  前台用户ID
     * @param newPassword  前台用户新密码
     * @return  成功状态
     */
    public boolean resetPassword(int userId, String newPassword);

    /**
     * 查询管理员用户
     * @param isAdmin 是否是管理员
     * @return  管理员用户
     */
    public List<GkUser> findByAdmin(boolean isAdmin);
}
