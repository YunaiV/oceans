package cn.iocoder.user.api;

import cn.iocoder.occeans.core.exception.ServiceException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public interface OAuth2Service {

    OAuth2AccessToken getAccessToken(String clientId, String clientSecret, String username, String password)
            throws ServiceException;

    OAuth2Authentication checkToken(String clientId, String clientSecret, String token)
            throws ServiceException;

}