package com.goku.oauth.service.facade;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.MapQuery;
import com.goku.oauth.entity.OauthAccessTokenWithBLOBs;
import com.goku.oauth.model.GkAccessToken;
import com.goku.oauth.service.GkAccessTokenService;
import com.goku.oauth.service.OauthAccessTokenService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
@Service("gkAccessTokenService")
public class GkAccessTokenServiceImpl implements GkAccessTokenService {
    @Autowired
    private OauthAccessTokenService oauthAccessTokenService;
    @Override
    public GkAccessToken queryTokenByTokenId(String tokenId) {
        MapQuery mapQuery = MapQuery.create("tokenId",tokenId);
        mapQuery.put("orderByClause","create_time DESC");//时间排序
        List<OauthAccessTokenWithBLOBs> accessTokenList = oauthAccessTokenService.selectByQueryWithBLOBs(mapQuery);
        if(CollectionUtils.isNotEmpty(accessTokenList)){
            GkAccessToken acAccessToken = new GkAccessToken();
            BeanUtils.copyProperties(accessTokenList.get(0),acAccessToken,"authentication");
            return acAccessToken;
        }
        return null;
    }
    
    @Override
    public GkAccessToken queryAuthenticationByTokenId(String tokenId) {
        MapQuery mapQuery = MapQuery.create("tokenId",tokenId);
        mapQuery.put("orderByClause","create_time DESC");//时间排序
        List<OauthAccessTokenWithBLOBs> accessTokenList = oauthAccessTokenService.selectByQueryWithBLOBs(mapQuery);
        if(CollectionUtils.isNotEmpty(accessTokenList)){
            GkAccessToken acAccessToken = new GkAccessToken();
            BeanUtils.copyProperties(accessTokenList.get(0), acAccessToken, "token");//不需要的不用传
            return acAccessToken;
        }
        return null;
    }
    
    @Override
    public GkAccessToken queryTokenByAuthenticationId(String authenticationId) {
        MapQuery mapQuery = MapQuery.create("authenticationId",authenticationId);
        mapQuery.put("orderByClause","create_time DESC");//时间排序
        List<OauthAccessTokenWithBLOBs> accessTokenList = oauthAccessTokenService.selectByQueryWithBLOBs(mapQuery);
        if(CollectionUtils.isNotEmpty(accessTokenList)){
            GkAccessToken acAccessToken = new GkAccessToken();
            BeanUtils.copyProperties(accessTokenList.get(0), acAccessToken, "authentication");//不需要的不用传
            return acAccessToken;
        }
        return null;
    }

    @Override
    public List<GkAccessToken> queryTokenByUserName(String userName) {
        if(StringUtils.isEmpty(userName)){
            return null;
        }
        MapQuery mapQuery = MapQuery.create("userName",userName);
        return queryAccessToken(mapQuery);
    }
    
    public List<GkAccessToken> queryAccessToken(MapQuery mapQuery){
        List<OauthAccessTokenWithBLOBs> oauthAccessTokenWithBLOBsList = oauthAccessTokenService.selectByQueryWithBLOBs(mapQuery);
        if(CollectionUtils.isNotEmpty(oauthAccessTokenWithBLOBsList)){
            List<GkAccessToken> acAccessTokenList = new ArrayList<>(oauthAccessTokenWithBLOBsList.size());
            for(OauthAccessTokenWithBLOBs oauthAccessToken : oauthAccessTokenWithBLOBsList) {
                GkAccessToken acAccessToken = new GkAccessToken();
                BeanUtils.copyProperties(oauthAccessToken, acAccessToken, "authentication");//不需要的不用传
                acAccessTokenList.add(acAccessToken);
            }
            return acAccessTokenList;
        }
        return null;
    }
    
    @Override
    public List<GkAccessToken> queryTokenByClientId(String clientId) {
        if(StringUtils.isEmpty(clientId)){
            return null;
        }
        MapQuery mapQuery = MapQuery.create("clientId",clientId);
//        mapQuery.put("orderByClause","create_time DESC");//时间排序
        return queryAccessToken(mapQuery);
    }

    @Override
    public boolean deleteByTokenId(String tokenId) {
        MapQuery mapQuery = MapQuery.create("tokenId",tokenId);
        return oauthAccessTokenService.deleteByQuery(mapQuery) != 0;
    }
    
    @Override
    public boolean create(String tokenId, byte[] token, String authenticationId, String userName, String clientId, byte[] authentication, String refreshToken) {
        OauthAccessTokenWithBLOBs record = new OauthAccessTokenWithBLOBs();
        record.setTokenId(tokenId);
        record.setAuthenticationId(authenticationId);
        record.setUserName(userName);
        record.setClientId(clientId);
        record.setAuthentication(authentication);
        record.setRefreshToken(refreshToken);
        record.setToken(token);
        return oauthAccessTokenService.insertSelective(record) != 0 ;
    }

    @Override
    public boolean deleteByRefreshTokenId(String refreshTokenId) {
        MapQuery mapQuery = MapQuery.create("refreshToken",refreshTokenId);
        return oauthAccessTokenService.deleteByQuery(mapQuery) != 0;
    }

    @Override
    public void deleteByPkId(Integer id) {
        int rt = oauthAccessTokenService.deleteByPrimaryKey(id);
        if( rt == 0){
            throw new ServiceException("删除AccessToken异常！");
        }
    }
}
