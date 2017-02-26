package com.goku.oauth.service.facade;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gkframework.orm.mybatis.query.MapQuery;
import com.goku.oauth.entity.OauthCode;
import com.goku.oauth.model.GkOauthCode;
import com.goku.oauth.service.GkOauthCodeService;
import com.goku.oauth.service.OauthCodeService;

import java.util.List;

/**
 * User: user
 * Date: 15/11/9
 * Version: 1.0
 */
@Service("gkOauthCodeService")
public class GkOauthCodeServiceImpl implements GkOauthCodeService {
    @Autowired
    private OauthCodeService oauthCodeService;
    @Override
    public void create(String code, byte[] authentication) {
        OauthCode oauthCode = new OauthCode();
        oauthCode.setCode(code);
        oauthCode.setAuthentication(authentication);
        oauthCodeService.insertSelective(oauthCode);
    }

    @Override
    public GkOauthCode queryByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        MapQuery mapQuery = MapQuery.create("code",code);
        List<OauthCode>  oauthCodeList = oauthCodeService.selectByQuery(mapQuery);
        if(CollectionUtils.isNotEmpty(oauthCodeList)){
            OauthCode oauthCode = oauthCodeList.get(0);
            GkOauthCode acOauthCode = new GkOauthCode();
            BeanUtils.copyProperties(oauthCode,acOauthCode);
            return acOauthCode;
        }
        return null;
    }

    @Override
    public void deleteByPkId(Integer id) {
        oauthCodeService.deleteByPrimaryKey(id);
    }
}
