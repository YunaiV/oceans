package cn.iocoder.user.service.base;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

public class AuthorizationExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        // TODO 芋艿，需要寻求更好的解决方案。需要一起改造 OAuth2ServiceImpl#throwsServiceExceptionIfError(...) 方法
        if (e instanceof UsernameNotFoundException) {
            e = new InvalidGrantException("Username not found", e);
        } else if (!(e instanceof OAuth2Exception)) {
            e = new OAuth2Exception(e.getMessage(), e);
        }
        return super.translate(e);
    }

}