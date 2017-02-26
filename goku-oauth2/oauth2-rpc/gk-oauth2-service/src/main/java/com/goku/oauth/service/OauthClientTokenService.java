package com.goku.oauth.service;

import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.oauth.entity.OauthClientToken;

import java.util.List;

public interface OauthClientTokenService {
    int countByQuery(Query query) throws ServiceException;

    OauthClientToken selectByPrimaryKey(Integer id) throws ServiceException;

    List<OauthClientToken> selectByQuery(Query query) throws ServiceException;

    int deleteByPrimaryKey(Integer id) throws ServiceException;

    int updateByPrimaryKeySelective(OauthClientToken record) throws ServiceException;

    int updateByPrimaryKey(OauthClientToken record) throws ServiceException;

    int deleteByQuery(Query query) throws ServiceException;

    int updateByMapSelective(OauthClientToken record, Query query) throws ServiceException;

    int updateByMap(OauthClientToken record, Query query) throws ServiceException;

    int insertSelective(OauthClientToken record) throws ServiceException;
}