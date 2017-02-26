package com.goku.oauth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.oauth.dao.OauthAccessTokenMapper;
import com.goku.oauth.entity.OauthAccessToken;
import com.goku.oauth.entity.OauthAccessTokenWithBLOBs;
import com.goku.oauth.service.OauthAccessTokenService;

import java.util.List;

@Service("oauthAccessTokenService")
public class OauthAccessTokenServiceImpl implements OauthAccessTokenService {
    @Autowired
    private OauthAccessTokenMapper oauthAccessTokenMapper;

    private static final Logger logger = LoggerFactory.getLogger(OauthAccessTokenServiceImpl.class);

    public int countByQuery(Query query) throws ServiceException {
        int count = this.oauthAccessTokenMapper.countByQuery(query);
        logger.debug("count: {}", count);
        return count;
    }

    public OauthAccessToken selectByPrimaryKey(Integer id) throws ServiceException {
        return this.oauthAccessTokenMapper.selectByPrimaryKey(id);
    }

    public List<OauthAccessToken> selectByQuery(Query query) throws ServiceException {
        return null; ///this.oauthAccessTokenMapper.selectByQueryWithoutBLOBs(query);
    }

    public int deleteByPrimaryKey(Integer id) throws ServiceException {
        return this.oauthAccessTokenMapper.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(OauthAccessToken record) throws ServiceException {
        return 0;//this.oauthAccessTokenMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(OauthAccessToken record) throws ServiceException {
        return 0;//this.oauthAccessTokenMapper.updateByPrimaryKeyWithoutBLOBs(record);
    }

    public int deleteByQuery(Query query) throws ServiceException {
        return this.oauthAccessTokenMapper.deleteByQuery(query);
    }

    public int updateByMapSelective(OauthAccessTokenWithBLOBs record, Query query) throws ServiceException {
        return this.oauthAccessTokenMapper.updateByMapSelective(record, query);
    }

    public int updateByMap(OauthAccessToken record, Query query) throws ServiceException {
        return 0;// this.oauthAccessTokenMapper.updateByMapWithoutBLOBs(record, query);
    }

    public int insertSelective(OauthAccessTokenWithBLOBs record) throws ServiceException {
        return this.oauthAccessTokenMapper.insertSelective(record);
    }

    public  List<OauthAccessTokenWithBLOBs> selectByQueryWithBLOBs(Query query) throws ServiceException {
        return this.oauthAccessTokenMapper.selectByQueryWithBLOBs(query);
    }
}