package com.goku.oauth.provider.code;

import com.goku.oauth.model.GkOauthCode;
import com.goku.oauth.service.GkOauthCodeService;
import com.goku.oauth.util.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.code.AuthorizationRequestHolder;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

/**
 * User: user
 * Date: 15/11/8
 * Version: 1.0
 */
public class SECAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    @Autowired
    GkOauthCodeService gkOauthCodeService;
    private RandomValueStringGenerator generator = new RandomValueStringGenerator();
    
    /**
     * 自定义TokenId
     *
     * @param authentication
     * @return
     */
    public String createAuthorizationCode(AuthorizationRequestHolder authentication) {

        String code = generator.generate();
        store(code, authentication);
        return code;
    }

    @Override
    protected void store(String code, AuthorizationRequestHolder authentication) {
        gkOauthCodeService.create(code, SerializationUtils.serialize(authentication));
//        jdbcTemplate.update(insertAuthenticationSql,
//                new Object[] { code, new SqlLobValue(SerializationUtils.serialize(authentication)) }, new int[] {
//                        Types.VARCHAR, Types.BLOB });
    }
    
    @Override
    protected AuthorizationRequestHolder remove(String code) {
        AuthorizationRequestHolder authentication = null;

        GkOauthCode acOauthCode = gkOauthCodeService.queryByCode(code);
        if (null == acOauthCode) {
            return null;
        }
        try {
            authentication = SerializationUtils.deserialize(acOauthCode.getAuthentication());
        } catch (IllegalArgumentException ignore) {
            ignore.printStackTrace();
        }

        if (authentication != null) {
            gkOauthCodeService.deleteByPkId(acOauthCode.getId());
        }

        return authentication;
    }
}
