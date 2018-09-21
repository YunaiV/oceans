package cn.iocoder.user.service.impl;

import cn.iocoder.occeans.core.exception.ServiceException;
import cn.iocoder.user.api.OAuth2Service;
import cn.iocoder.user.api.constants.ErrorCodeConstants;
import cn.iocoder.user.api.dto.OAuth2AccessTokenDTO;
import cn.iocoder.user.api.dto.OAuth2AuthenticationDTO;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    private RestTemplate restTemplate;
    private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

    public OAuth2ServiceImpl() {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) { // Ignore 400 。当授权失败时，会响应该状态码们。如果不忽略，会抛出异常。
                    super.handleError(response);
                }
            }

        });
    }

    @Override
    public OAuth2AccessToken getAccessToken(String clientId, String clientSecret, String mobile, String password) {
        // 构建请求数据
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("username", mobile);
        formData.add("password", password);
        formData.add("grant_type", "password");
        // 构造请求头
        HttpHeaders headers = createHttpHeaders(clientId, clientSecret);
        // 执行请求
        // TODO 芋艿，需要重构下
        OAuth2AccessToken token = restTemplate.exchange("http://127.0.0.1:8081/oauth/token", HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, String>>(formData, headers), OAuth2AccessToken.class).getBody();
        // 校验响应结果
        throwsServiceExceptionIfError(token.getAdditionalInformation());
        return token;
    }

    @Override
    public OAuth2Authentication checkToken(String clientId, String clientSecret, String token) {
        // 构建请求数据
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("token", token);
        // 构造请求头
        HttpHeaders headers = createHttpHeaders(clientId, clientSecret);
        // 执行请求
        // TODO 芋艿，需要重构下
        Map<String, ?> map = restTemplate.exchange("http://127.0.0.1:8081/oauth/check_token", HttpMethod.POST,
                new HttpEntity<>(formData, headers), Map.class).getBody();
        // 校验响应结果
        throwsServiceExceptionIfError(map);
        // 转换
        return tokenConverter.extractAuthentication(map);
    }

    private String getAuthorizationHeader(String clientId, String clientSecret) {
        String credentials = String.format("%s:%s", clientId, clientSecret);
        try {
            return "Basic " + new String(Base64.encode(credentials.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Could not convert String");
        }
    }

    private HttpHeaders createHttpHeaders(String clientId, String clientSecret) {
        HttpHeaders headers = new HttpHeaders();
        // Authorization header
        headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));
        // ContentType header
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        return headers;
    }

    // TODO 芋艿，捉摸有没更好的实现方式
    private void throwsServiceExceptionIfError(Map<String, ?> errors) {
        String error = (String) errors.get(OAuth2Exception.ERROR);
        if (error == null) {
            return;
        }
        String description = (String) errors.get(OAuth2Exception.DESCRIPTION);
        OAuth2Exception exception = OAuth2Exception.create(error, description);
        ServiceException ex = null;
        if (exception instanceof InvalidGrantException) {
            switch (description) {
                case "Bad credentials": // 来自 DaoAuthenticationProvider 的第 100 行代码
                    ex = new ServiceException(ErrorCodeConstants.OAUTH2_INVALID_GRANT_BAD_CREDENTIALS, "密码不正确");
                    break;
                case "Username not found": // 来自 AuthorizationExceptionTranslator
                    ex = new ServiceException(ErrorCodeConstants.OAUTH2_INVALID_GRANT_BAD_CREDENTIALS, "账号不存在");
                    break;
                default:
                    ex = new ServiceException(ErrorCodeConstants.OAUTH2_INVALID_GRANT, description);
                    break;
            }
        } else if (exception instanceof InvalidTokenException) {
            switch (description) {
                case "Token was not recognised": // 来自 CheckTokenEndpoint
                    ex = new ServiceException(ErrorCodeConstants.OAUTH_INVALID_TOKEN_NOT_FOUND, "访问令牌不存在");
                    break;
                case "Token has expired": // 来自 CheckTokenEndpoint
                    ex = new ServiceException(ErrorCodeConstants.OAUTH_INVALID_TOKEN_EXPIRED, "账号不存在");
                    break;
                default:
                    ex = new ServiceException(ErrorCodeConstants.OAUTH_INVALID_TOKEN, description);
                    break;
            }
        }
        // 默认未知错误
        if (ex == null) {
            ex = new ServiceException(ErrorCodeConstants.OAUTH2_UNKNOWN, description);
        }
        // 抛出异常
        throw ex;
    }


    @Override
    public OAuth2AccessTokenDTO getAccessToken(String mobile, String code) {
        return null;
    }

    @Override
    public OAuth2AuthenticationDTO checkToken(String accessToken) throws ServiceException {
        return null;
    }

}
