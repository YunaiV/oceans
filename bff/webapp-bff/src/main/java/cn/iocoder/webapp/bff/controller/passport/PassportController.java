package cn.iocoder.webapp.bff.controller.passport;

import cn.iocoder.user.api.OAuth2Service;
import cn.iocoder.user.api.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passport")
public class PassportController {

    @Value("${security.oauth2.client.id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Reference
    private OAuth2Service oauth2Service;
    @Reference
    private UserService userService;

    @PostMapping("/mobile/pwd/login")
    public OAuth2AccessToken mobileLogin(@RequestParam("mobile") String mobile,
                                         @RequestParam("password") String password) {
        return oauth2Service.getAccessToken(clientId, clientSecret, mobile, password);
    }

    @PostMapping("/mobile/code/login")
    public String mobileRegister(@RequestParam("mobile") String mobile,
                                 @RequestParam("code") String code) {
        return null;
    }

    @PostMapping("/qq/login")
    public String qqLogin() {
        return null;
    }

    @PostMapping("/qq/bind")
    public String qqBind() {
        return null;
    }

}