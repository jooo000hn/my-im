package com.goku.oauth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.oauth.dao.OauthCodeMapper;
import com.goku.oauth.entity.OauthCode;
import com.goku.oauth.service.OauthCodeService;

import java.util.List;

@Service("oauthCodeService")
public class OauthCodeServiceImpl implements OauthCodeService {
    @Autowired
    private OauthCodeMapper oauthCodeMapper;

    private static final Logger logger = LoggerFactory.getLogger(OauthCodeServiceImpl.class);

    public int countByQuery(Query query) throws ServiceException {
        int count = this.oauthCodeMapper.countByQuery(query);
        logger.debug("count: {}", count);
        return count;
    }

    public OauthCode selectByPrimaryKey(Integer id) throws ServiceException {
        return this.oauthCodeMapper.selectByPrimaryKey(id);
    }

    public List<OauthCode> selectByQuery(Query query) throws ServiceException {
        return null;// this.oauthCodeMapper.selectByQueryWithoutBLOBs(query);
    }

    public int deleteByPrimaryKey(Integer id) throws ServiceException {
        return this.oauthCodeMapper.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(OauthCode record) throws ServiceException {
        return this.oauthCodeMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(OauthCode record) throws ServiceException {
        return 0;//this.oauthCodeMapper.updateByPrimaryKeyWithoutBLOBs(record);
    }

    public int deleteByQuery(Query query) throws ServiceException {
        return this.oauthCodeMapper.deleteByQuery(query);
    }

    public int updateByMapSelective(OauthCode record, Query query) throws ServiceException {
        return this.oauthCodeMapper.updateByMapSelective(record, query);
    }

    public int updateByMap(OauthCode record, Query query) throws ServiceException {
        return 0;//this.oauthCodeMapper.updateByMapWithoutBLOBs(record, query);
    }

    public int insertSelective(OauthCode record) throws ServiceException {
        return this.oauthCodeMapper.insertSelective(record);
    }
}