package com.goku.oauth.token;

import com.goku.user.service.GkUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Shengzhao Li
 */
@Controller
@RequestMapping("/protected/")
public class MobileController {

    @Autowired
    private GkUserService userService;

    @RequestMapping(value = "tokencheck", method = RequestMethod.GET)
    @ResponseBody
    public String getGreeting() {
        return "OK";
    }
    
    @RequestMapping("user_info")
    public void userInfo(HttpServletResponse response) throws Exception {
//        final UserJsonDto jsonDto = userService.loadCurrentUserJsonDto();
//        writeJson(response, JSONObject.fromObject(jsonDto));
    }

}