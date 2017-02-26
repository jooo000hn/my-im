package com.goku.oauth.service;

import com.gkframework.exception.ServiceException;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.oauth.entity.OauthCode;

import java.util.List;

public interface OauthCodeService {
    int countByQuery(Query query) throws ServiceException;

    OauthCode selectByPrimaryKey(Integer id) throws ServiceException;

    List<OauthCode> selectByQuery(Query query) throws ServiceException;

    int deleteByPrimaryKey(Integer id) throws ServiceException;

    int updateByPrimaryKeySelective(OauthCode record) throws ServiceException;

    int updateByPrimaryKey(OauthCode record) throws ServiceException;

    int deleteByQuery(Query query) throws ServiceException;

    int updateByMapSelective(OauthCode record, Query query) throws ServiceException;

    int updateByMap(OauthCode record, Query query) throws ServiceException;

    int insertSelective(OauthCode record) throws ServiceException;
}