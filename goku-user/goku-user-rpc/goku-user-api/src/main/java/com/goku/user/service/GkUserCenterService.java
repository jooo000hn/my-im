package com.goku.user.service;

import com.goku.user.model.GkUser;

import java.util.List;
import java.util.Map;

/**
 * Created by user on 15/12/5.
 */
public interface GkUserCenterService {
    /**
     * 给统计平台提供的多用户查询接口
     * @param pUserIds 多个平台的id
     * @return  返回对应的多用户信息
     */
    Map<Integer,Map<String,String>> searchUserNameByPuserIds(List<Integer> pUserIds);

    /**
     * 根据多平台id查询对应的所有用户信息
     * @param pUserIds  多个平台的id
     * @return  返回用户信息集
     */
    List<GkUser> queryUsersByPuserIds(List<Integer> pUserIds);
}
