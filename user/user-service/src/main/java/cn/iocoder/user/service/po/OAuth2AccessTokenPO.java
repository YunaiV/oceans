package cn.iocoder.user.service.po;

import cn.iocoder.occeans.core.po.BasePO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class OAuth2AccessTokenPO extends BasePO {

    /**
     * 访问令牌
     */
    private String tokenId;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 用户编号
     */
    private Long uid;
    /**
     * 过期时间
     */
    private Date expiresTime;
    /**
     * 是否有效
     */
    private Boolean valid;

}