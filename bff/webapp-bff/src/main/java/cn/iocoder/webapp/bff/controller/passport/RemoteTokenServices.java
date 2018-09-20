package cn.iocoder.webapp.bff.controller.passport;

import cn.iocoder.user.api.OAuth2Service;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

public class RemoteTokenServices implements ResourceServerTokenServices {

    @Reference
    private OAuth2Service oauth2Service;

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        return oauth2Service.checkToken("fooClientIdPassword", "secret", accessToken);
        // TODO 芋艿，需要考虑下，是否可以去掉
//        if (map.containsKey("error")) {
//            // TODO 打印错误日志
////            logger.debug("check_token returned error: " + map.get("error"));
//            throw new InvalidTokenException(accessToken);
//        }
//
//        Assert.state(map.containsKey("client_id"), "Client id must be present in response from auth server");
//        return tokenConverter.extractAuthentication(map);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

}
