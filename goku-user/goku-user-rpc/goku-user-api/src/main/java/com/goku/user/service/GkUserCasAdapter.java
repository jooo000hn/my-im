package com.goku.user.service;

/**
 * 接口
 * User: moueimei
 * Date: 15/10/23
 * Version: 1.0
 */
public interface GkUserCasAdapter {


    /**
     * 加载用户信息
     * @param puId
     * @return
     */
    public String loadUserInfo(int puId) throws Exception;
    
    /**
     *  编辑用户信息
     * @param puId  用户平台id
     * @param nick      昵称
     * @param sex  性别
     * @param age  年龄
     * @param sign 签名
     * @param birthday 生日
     * @param corientation  性取向
     * @return
     */
    public String submitUserInfo(int puId, String nick, String sex, int age, String sign, String birthday, int corientation) throws Exception;
    
    /**
     * 注册
     * @param phone  手机号
     * @param password  密码
     * @param createBy
     * @return
     * @throws Exception
     */
    public String registerPhone(String phone, String password, String createBy)  throws Exception;

}
