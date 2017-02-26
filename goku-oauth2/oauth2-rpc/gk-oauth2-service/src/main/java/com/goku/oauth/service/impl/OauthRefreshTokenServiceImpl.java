package com.goku.oauth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.oauth.dao.OauthRefreshTokenMapper;
import com.goku.oauth.entity.OauthRefreshToken;
import com.goku.oauth.entity.OauthRefreshTokenWithBLOBs;
import com.goku.oauth.service.OauthRefreshTokenService;

import java.util.List;

@Service("oauthRefreshTokenService")
public class OauthRefreshTokenServiceImpl implements OauthRefreshTokenService {
    @Autowired
    private OauthRefreshTokenMapper oauthRefreshTokenMapper;

    private static final Logger logger = LoggerFactory.getLogger(OauthRefreshTokenServiceImpl.class);

    public int countByQuery(Query query) throws ServiceException {
        int count = this.oauthRefreshTokenMapper.countByQuery(query);
        logger.debug("count: {}", count);
        return count;
    }

    public OauthRefreshToken selectByPrimaryKey(Integer id) throws ServiceException {
        return this.oauthRefreshTokenMapper.selectByPrimaryKey(id);
    }

    public List<OauthRefreshToken> selectByQuery(Query query) throws ServiceException {
        return null;// this.oauthRefreshTokenMapper.selectByQueryWithoutBLOBs(query);
    }

    public int deleteByPrimaryKey(Integer id) throws ServiceException {
        return this.oauthRefreshTokenMapper.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(OauthRefreshToken record) throws ServiceException {
        return 0;//this.oauthRefreshTokenMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(OauthRefreshToken record) throws ServiceException {
        return 0;//this.oauthRefreshTokenMapper.updateByPrimaryKeyWithoutBLOBs(record);
    }

    public int deleteByQuery(Query query) throws ServiceException {
        return this.oauthRefreshTokenMapper.deleteByQuery(query);
    }

    public int updateByMapSelective(OauthRefreshToken record, Query query) throws ServiceException {
        return 0;// this.oauthRefreshTokenMapper.updateByMapSelective(record, query);
    }

    public int updateByMap(OauthRefreshToken record, Query query) throws ServiceException {
        return 0;// this.oauthRefreshTokenMapper.updateByMapWithoutBLOBs(record, query);
    }

    public int insertSelective(OauthRefreshTokenWithBLOBs record) throws ServiceException {
        return this.oauthRefreshTokenMapper.insertSelective(record);
    }

    public List<OauthRefreshTokenWithBLOBs> selectByQueryWithBLOBs(Query query) throws ServiceException{
        return this.oauthRefreshTokenMapper.selectByQueryWithBLOBs(query);
    }
}