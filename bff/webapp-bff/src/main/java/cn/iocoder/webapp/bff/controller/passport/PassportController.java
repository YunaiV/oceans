package cn.iocoder.webapp.bff.controller.passport;

import cn.iocoder.user.api.OAuth2Service;
import cn.iocoder.user.api.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passport")
public class PassportController {

    @Reference
    private OAuth2Service oauth2Service;
    @Reference
    private UserService userService;

    @PostMapping("/login")
    public OAuth2AccessToken login(@RequestParam("mobile") String mobile,
                        @RequestParam("password") String password) {
        OAuth2AccessToken token = oauth2Service.getAccessToken("fooClientIdPassword", "secret", mobile, password);
        return token;
    }

}