package cn.iocoder.oceans.user.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class OAuth2AuthenticationDTO implements Serializable {

    /**
     * 用户编号
     */
    private Long uid;

}