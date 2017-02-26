package com.goku.oauth.service.facade;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.MapQuery;
import com.goku.oauth.entity.OauthRefreshTokenWithBLOBs;
import com.goku.oauth.model.GkRefreshToken;
import com.goku.oauth.service.GkRefreshTokenService;
import com.goku.oauth.service.OauthRefreshTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: user
 * Date: 15/11/7
 * Version: 1.0
 */
@Service("gkRefreshTokenService")
public class GkRefreshTokenServiceImpl implements GkRefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(GkRefreshTokenServiceImpl.class);
    @Autowired
    OauthRefreshTokenService oauthRefreshTokenService;
    @Override
    public boolean create(String tokenId, byte[] token, byte[] authentication) {
        OauthRefreshTokenWithBLOBs oauthRefreshTokenWithBLOBs = new OauthRefreshTokenWithBLOBs();
        oauthRefreshTokenWithBLOBs.setTokenId(tokenId);
        oauthRefreshTokenWithBLOBs.setToken(token);
        oauthRefreshTokenWithBLOBs.setAuthentication(authentication);
        int rt = oauthRefreshTokenService.insertSelective(oauthRefreshTokenWithBLOBs);
        logger.debug("create rt:{}",rt);
        if(rt == 0 ){
            throw new ServiceException("创建AcRefreshToken异常");
        }
        return true ;
    }

    @Override
    public GkRefreshToken queryTokenByRefreshTokenId(String tokenId) {
        MapQuery mapQuery = MapQuery.create("tokenId",tokenId);
        mapQuery.put("orderByClause","create_time DESC");//时间排序
        List<OauthRefreshTokenWithBLOBs> refreshTokenWithBLOBsList = oauthRefreshTokenService.selectByQueryWithBLOBs(mapQuery);
        if(CollectionUtils.isNotEmpty(refreshTokenWithBLOBsList)){
            GkRefreshToken acRefreshToken = new GkRefreshToken();
            BeanUtils.copyProperties(refreshTokenWithBLOBsList.get(0), acRefreshToken, "authentication");//不需要的不用传
            return acRefreshToken;
        }
        return null;
    }

    @Override
    public GkRefreshToken queryAuthenticationByRefreshTokenId(String tokenId) {
        MapQuery mapQuery = MapQuery.create("tokenId",tokenId);
        mapQuery.put("orderByClause","create_time DESC");//时间排序
        List<OauthRefreshTokenWithBLOBs> refreshTokenWithBLOBsList = oauthRefreshTokenService.selectByQueryWithBLOBs(mapQuery);
        if(CollectionUtils.isNotEmpty(refreshTokenWithBLOBsList)){
            GkRefreshToken acRefreshToken = new GkRefreshToken();
            BeanUtils.copyProperties(refreshTokenWithBLOBsList.get(0), acRefreshToken, "token");//不需要的不用传
            return acRefreshToken;
        }
        return null;
    }


    @Override
    public void deleteByPkId(Integer id) {
        int rt = oauthRefreshTokenService.deleteByPrimaryKey(id);
        if( rt == 0){
            throw new ServiceException("通过id删除refreshToken异常！");
        }
    }

    @Override
    public boolean deleteByRefreshTokenId(String tokenId) {
        MapQuery mapQuery = MapQuery.create("tokenId",tokenId);
        int rt = oauthRefreshTokenService.deleteByQuery(mapQuery);
        if( rt == 0){
            throw new ServiceException("通过token_id删除refreshToken异常！");
        }
        return true;
    }
}
