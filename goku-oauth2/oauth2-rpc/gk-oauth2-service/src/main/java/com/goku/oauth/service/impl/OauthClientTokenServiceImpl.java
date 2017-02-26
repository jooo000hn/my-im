package com.goku.oauth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.oauth.dao.OauthClientTokenMapper;
import com.goku.oauth.entity.OauthClientToken;
import com.goku.oauth.service.OauthClientTokenService;

import java.util.List;

@Service("oauthClientTokenService")
public class OauthClientTokenServiceImpl implements OauthClientTokenService {
    @Autowired
    private OauthClientTokenMapper oauthClientTokenMapper;

    private static final Logger logger = LoggerFactory.getLogger(OauthClientTokenServiceImpl.class);

    public int countByQuery(Query query) throws ServiceException {
        int count = this.oauthClientTokenMapper.countByQuery(query);
        logger.debug("count: {}", count);
        return count;
    }

    public OauthClientToken selectByPrimaryKey(Integer id) throws ServiceException {
        return this.oauthClientTokenMapper.selectByPrimaryKey(id);
    }

    public List<OauthClientToken> selectByQuery(Query query) throws ServiceException {
        return null;//this.oauthClientTokenMapper.selectByQueryWithoutBLOBs(query);
    }

    public int deleteByPrimaryKey(Integer id) throws ServiceException {
        return this.oauthClientTokenMapper.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(OauthClientToken record) throws ServiceException {
        return this.oauthClientTokenMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(OauthClientToken record) throws ServiceException {
        return 0;// this.oauthClientTokenMapper.updateByPrimaryKeyWithoutBLOBs(record);
    }

    public int deleteByQuery(Query query) throws ServiceException {
        return this.oauthClientTokenMapper.deleteByQuery(query);
    }

    public int updateByMapSelective(OauthClientToken record, Query query) throws ServiceException {
        return this.oauthClientTokenMapper.updateByMapSelective(record, query);
    }

    public int updateByMap(OauthClientToken record, Query query) throws ServiceException {
        return 0;//this.oauthClientTokenMapper.updateByMapWithoutBLOBs(record, query);
    }

    public int insertSelective(OauthClientToken record) throws ServiceException {
        return this.oauthClientTokenMapper.insertSelective(record);
    }
}