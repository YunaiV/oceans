package cn.iocoder.oceans.user.service.impl;

import cn.iocoder.occeans.core.exception.ServiceException;
import cn.iocoder.occeans.core.util.ServiceExceptionUtil;
import cn.iocoder.oceans.user.api.OAuth2Service;
import cn.iocoder.oceans.user.api.constants.ErrorCodeEnum;
import cn.iocoder.oceans.user.api.dto.OAuth2AccessTokenDTO;
import cn.iocoder.oceans.user.api.dto.OAuth2AuthenticationDTO;
import cn.iocoder.oceans.user.service.dao.OAuth2AccessTokenMapper;
import cn.iocoder.oceans.user.service.dao.OAuth2RefreshTokenMapper;
import cn.iocoder.oceans.user.service.po.MobileCodePO;
import cn.iocoder.oceans.user.service.po.OAuth2AccessTokenPO;
import cn.iocoder.oceans.user.service.po.OAuth2RefreshTokenPO;
import cn.iocoder.oceans.user.service.po.UserPO;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * OAuth2Service ，实现用户授权相关的逻辑
 */
@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    /**
     * 访问令牌过期时间，单位：毫秒
     */
    @Value("modules.oauth2-code-service.access-token-expire-time-millis")
    private int accessTokenExpireTimeMillis;
    /**
     * 刷新令牌过期时间，单位：毫秒
     */
    @Value("modules.oauth2-code-service.refresh-token-expire-time-millis")
    private int refreshTokenExpireTimeMillis;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private MobileCodeServiceImpl mobileCodeService;
    @Autowired
    private OAuth2AccessTokenMapper oauth2AccessTokenMapper;
    @Autowired
    private OAuth2RefreshTokenMapper oauth2RefreshTokenMapper;

    @Override
    @Transactional
    public OAuth2AccessTokenDTO getAccessToken(String mobile, String code) {
        // 校验手机号的最后一个手机验证码是否有效
        MobileCodePO mobileCodePO = mobileCodeService.validLastMobileCode(mobile, code);
        // 获取用户
        UserPO userPO = userService.getUser(mobile);
        if (userPO == null) { // 用户不存在
            throw ServiceExceptionUtil.exception(ErrorCodeEnum.USER_MOBILE_NOT_REGISTERED.getCode());
        }
        // 创建刷新令牌
        OAuth2RefreshTokenPO oauth2RefreshTokenPO = createOAuth2RefreshToken(userPO.getUid());
        // 创建访问令牌
        OAuth2AccessTokenPO oauth2AccessTokenPO = createOAuth2AccessToken(userPO.getUid(), oauth2RefreshTokenPO.getTokenId());
        // 标记已使用
        mobileCodeService.useMobileCode(mobileCodePO.getId(), userPO.getUid());
        // 转换返回
        return new OAuth2AccessTokenDTO().setAccessToken(oauth2AccessTokenPO.getTokenId())
                .setRefreshToken(oauth2RefreshTokenPO.getTokenId())
                .setExpiresIn(Math.max((int) ((oauth2AccessTokenPO.getExpiresTime().getTime() - System.currentTimeMillis()) / 1000), 0));
    }

    @Override
    public OAuth2AuthenticationDTO checkToken(String accessToken) throws ServiceException {
        OAuth2AccessTokenPO accessTokenPO = oauth2AccessTokenMapper.selectByTokenId(accessToken);
        if (accessTokenPO == null) { // 不存在
            throw ServiceExceptionUtil.exception(ErrorCodeEnum.OAUTH_INVALID_TOKEN_NOT_FOUND.getCode());
        }
        if (accessTokenPO.getExpiresTime().getTime() > System.currentTimeMillis()) { // 已过期
            throw ServiceExceptionUtil.exception(ErrorCodeEnum.OAUTH_INVALID_TOKEN_EXPIRED.getCode());
        }
        if (!accessTokenPO.getValid()) { // 无效
            throw ServiceExceptionUtil.exception(ErrorCodeEnum.OAUTH_INVALID_TOKEN_INVALID.getCode());
        }
        // 转换返回
        return new OAuth2AuthenticationDTO().setUid(accessTokenPO.getUid());
    }

    private OAuth2AccessTokenPO createOAuth2AccessToken(Long uid, String refreshToken) {
        OAuth2AccessTokenPO accessToken = new OAuth2AccessTokenPO().setTokenId(generateAccessToken())
                .setRefreshToken(refreshToken)
                .setUid(uid)
                .setExpiresTime(new Date(System.currentTimeMillis() + accessTokenExpireTimeMillis))
                .setValid(true);
        oauth2AccessTokenMapper.insert(accessToken);
        return accessToken;
    }

    private OAuth2RefreshTokenPO createOAuth2RefreshToken(Long uid) {
        OAuth2RefreshTokenPO refreshToken = new OAuth2RefreshTokenPO().setTokenId(generateRefreshToken())
                .setUid(uid)
                .setExpiresTime(new Date(System.currentTimeMillis() + refreshTokenExpireTimeMillis))
                .setValid(true);
        oauth2RefreshTokenMapper.insert(refreshToken);
        return refreshToken;
    }

    private String generateAccessToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String generateRefreshToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
