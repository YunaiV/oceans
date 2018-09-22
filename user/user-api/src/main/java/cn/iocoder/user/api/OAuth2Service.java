package cn.iocoder.user.api;

import cn.iocoder.occeans.core.exception.ServiceException;
import cn.iocoder.user.api.dto.OAuth2AccessTokenDTO;
import cn.iocoder.user.api.dto.OAuth2AuthenticationDTO;

public interface OAuth2Service {

    /**
     * 使用手机号 + 验证码，获取访问令牌等信息
     *
     * 如果手机未注册，并且验证码正确，进行自动注册。
     *
     * @param mobile 手机号
     * @param code   验证码
     * @return 授权信息
     */
    OAuth2AccessTokenDTO getAccessToken(String mobile, String code)
            throws ServiceException;

    /**
     * 校验访问令牌，获取身份信息( 不包括 accessToken 等等 )
     *
     * @param accessToken 访问令牌
     * @return 授权信息
     */
    OAuth2AuthenticationDTO checkToken(String accessToken)
            throws ServiceException;

    // @see 刷新 token

    // @see 移除 token

}