package com.goku.oauth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.oauth.dao.OauthClientDetailsMapper;
import com.goku.oauth.entity.OauthClientDetails;
import com.goku.oauth.service.OauthClientDetailsService;

import java.util.List;

@Service("oauthClientDetailsService")
public class OauthClientDetailsServiceImpl implements OauthClientDetailsService {
    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;

    private static final Logger logger = LoggerFactory.getLogger(OauthClientDetailsServiceImpl.class);

    public int countByQuery(Query query) throws ServiceException {
        int count = this.oauthClientDetailsMapper.countByQuery(query);
        logger.debug("count: {}", count);
        return count;
    }

    public OauthClientDetails selectByPrimaryKey(String clientId) throws ServiceException {
        return this.oauthClientDetailsMapper.selectByPrimaryKey(clientId);
    }

    public List<OauthClientDetails> selectByQuery(Query query) throws ServiceException {
        return this.oauthClientDetailsMapper.selectByQuery(query);
    }

    public int deleteByPrimaryKey(String clientId) throws ServiceException {
        return this.oauthClientDetailsMapper.deleteByPrimaryKey(clientId);
    }

    public int updateByPrimaryKeySelective(OauthClientDetails record) throws ServiceException {
        return this.oauthClientDetailsMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(OauthClientDetails record) throws ServiceException {
        return this.oauthClientDetailsMapper.updateByPrimaryKey(record);
    }

    public int deleteByQuery(Query query) throws ServiceException {
        return this.oauthClientDetailsMapper.deleteByQuery(query);
    }

    public int updateByMapSelective(OauthClientDetails record, Query query) throws ServiceException {
        return this.oauthClientDetailsMapper.updateByMapSelective(record, query);
    }

    public int updateByMap(OauthClientDetails record, Query query) throws ServiceException {
        return this.oauthClientDetailsMapper.updateByMap(record, query);
    }

    public int insertSelective(OauthClientDetails record) throws ServiceException {
        return this.oauthClientDetailsMapper.insertSelective(record);
    }
}