package com.goku.oauth.service;

import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.oauth.entity.OauthClientDetails;

import java.util.List;

public interface OauthClientDetailsService {
    int countByQuery(Query query) throws ServiceException;

    OauthClientDetails selectByPrimaryKey(String clientId) throws ServiceException;

    List<OauthClientDetails> selectByQuery(Query query) throws ServiceException;

    int deleteByPrimaryKey(String clientId) throws ServiceException;

    int updateByPrimaryKeySelective(OauthClientDetails record) throws ServiceException;

    int updateByPrimaryKey(OauthClientDetails record) throws ServiceException;

    int deleteByQuery(Query query) throws ServiceException;

    int updateByMapSelective(OauthClientDetails record, Query query) throws ServiceException;

    int updateByMap(OauthClientDetails record, Query query) throws ServiceException;

    int insertSelective(OauthClientDetails record) throws ServiceException;
}