package cn.iocoder.oceans.user.service.po;

import cn.iocoder.oceans.core.po.BasePO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户注册信息
 */
@Data
@Accessors(chain = true)
public class UserRegisterPO extends BasePO {

    /**
     * 用户编号
     */
    private Long uid;

    // TODO 芋艿 ip
    // TODO 芋艿 ua
    // TODO 芋艿 方式，手机注册、qq 等等

}