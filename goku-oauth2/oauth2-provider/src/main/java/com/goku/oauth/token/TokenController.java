package com.goku.oauth.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.goku.oauth.provider.token.SECTokenServices;

/**
 * @author Shengzhao Li
 */
@Controller
@RequestMapping("/token/")
public class TokenController {

    @Autowired
    private SECTokenServices tokenServices;
    
    @RequestMapping(value = "revoke", method = RequestMethod.GET)
    @ResponseBody
    public String revoke(@RequestParam("oauthToken") String oauthToken) {
    	
        String success = "FAILED";
        if(tokenServices.revokeToken(oauthToken)){
            success = "SUCCESS";
        }
        return success;
    }
    
    @RequestMapping(value = "tokencheck", method = RequestMethod.GET)
    @ResponseBody
    public String getGreeting() {

        return "OK";

    }
}