package cn.iocoder.user.api.dto;

import lombok.Data;

@Data
public class OAuth2AccessTokenDTO {

    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 过期时间，单位：秒。
     */
    private Integer expiresIn;

}