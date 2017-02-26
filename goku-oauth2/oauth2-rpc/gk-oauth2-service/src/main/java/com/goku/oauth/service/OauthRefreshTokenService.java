package com.goku.oauth.service;

import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.oauth.entity.OauthRefreshToken;
import com.goku.oauth.entity.OauthRefreshTokenWithBLOBs;

import java.util.List;

public interface OauthRefreshTokenService {
    int countByQuery(Query query) throws ServiceException;

    OauthRefreshToken selectByPrimaryKey(Integer id) throws ServiceException;

    List<OauthRefreshToken> selectByQuery(Query query) throws ServiceException;

    int deleteByPrimaryKey(Integer id) throws ServiceException;

    int updateByPrimaryKeySelective(OauthRefreshToken record) throws ServiceException;

    int updateByPrimaryKey(OauthRefreshToken record) throws ServiceException;

    int deleteByQuery(Query query) throws ServiceException;

    int updateByMapSelective(OauthRefreshToken record, Query query) throws ServiceException;

    int updateByMap(OauthRefreshToken record, Query query) throws ServiceException;

    int insertSelective(OauthRefreshTokenWithBLOBs record) throws ServiceException;

    public List<OauthRefreshTokenWithBLOBs> selectByQueryWithBLOBs(Query query) throws ServiceException;
}