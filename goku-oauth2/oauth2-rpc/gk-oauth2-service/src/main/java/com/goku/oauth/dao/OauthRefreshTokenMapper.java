package com.goku.oauth.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.gkframework.orm.mybatis.query.Query;
import com.goku.oauth.entity.OauthRefreshToken;
import com.goku.oauth.entity.OauthRefreshTokenWithBLOBs;

import java.util.List;
import java.util.Map;

@Repository
public interface OauthRefreshTokenMapper {
    /**
     * 根据条件查询记录总数
     */
    int countByQuery(Query query);

    /**
     * 根据条件删除记录
     */
    int deleteByQuery(Query query);

    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(OauthRefreshTokenWithBLOBs record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(OauthRefreshTokenWithBLOBs record);

    /**
     * 根据条件查询记录集
     */
    List<OauthRefreshTokenWithBLOBs> selectByQueryWithBLOBs(Query query);

    /**
     * 根据条件查询记录集
     */
    List<OauthRefreshToken> selectByQuery(Query query);

    /**
     * 根据主键查询记录
     */
    OauthRefreshTokenWithBLOBs selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByMapSelective(@Param("record") OauthRefreshTokenWithBLOBs record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByQueryWithBLOBs(@Param("record") OauthRefreshTokenWithBLOBs record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByMap(@Param("record") OauthRefreshToken record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(OauthRefreshTokenWithBLOBs record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKeyWithBLOBs(OauthRefreshTokenWithBLOBs record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(OauthRefreshToken record);
}