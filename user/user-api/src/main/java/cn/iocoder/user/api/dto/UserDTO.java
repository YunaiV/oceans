package cn.iocoder.user.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDTO {

    /**
     * 用户编号
     */
    private Long uid;
    /**
     * 手机号
     */
    private String mobile;

}
