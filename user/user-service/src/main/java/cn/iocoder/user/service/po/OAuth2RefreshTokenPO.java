package cn.iocoder.user.service.po;

import cn.iocoder.occeans.core.po.BasePO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 刷新令牌
 *
 * idx_uid
 */
@Data
@Accessors(chain = true)
public class OAuth2RefreshTokenPO extends BasePO {

    /**
     * 刷新令牌
     */
    private String tokenId;
    /**
     * 用户编号
     */
    private Long uid;
    /**
     * 是否有效
     */
    private Boolean valid;
    /**
     * 过期时间
     */
    private Date expiresTime;

}
