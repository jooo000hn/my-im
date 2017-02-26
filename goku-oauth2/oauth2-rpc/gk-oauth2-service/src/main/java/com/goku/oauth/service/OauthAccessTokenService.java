package com.goku.oauth.service;

import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.oauth.entity.OauthAccessToken;
import com.goku.oauth.entity.OauthAccessTokenWithBLOBs;

import java.util.List;

public interface OauthAccessTokenService {
    int countByQuery(Query query) throws ServiceException;

    OauthAccessToken selectByPrimaryKey(Integer id) throws ServiceException;

    List<OauthAccessToken> selectByQuery(Query query) throws ServiceException;

    int deleteByPrimaryKey(Integer id) throws ServiceException;

    int updateByPrimaryKeySelective(OauthAccessToken record) throws ServiceException;

    int updateByPrimaryKey(OauthAccessToken record) throws ServiceException;

    int deleteByQuery(Query query) throws ServiceException;

    int updateByMapSelective(OauthAccessTokenWithBLOBs record, Query query) throws ServiceException;

    int updateByMap(OauthAccessToken record, Query query) throws ServiceException;

    int insertSelective(OauthAccessTokenWithBLOBs record) throws ServiceException;

    public  List<OauthAccessTokenWithBLOBs> selectByQueryWithBLOBs(Query query) throws ServiceException;
}