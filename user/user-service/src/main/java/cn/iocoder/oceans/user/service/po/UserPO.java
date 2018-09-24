package cn.iocoder.oceans.user.service.po;

import cn.iocoder.oceans.core.po.BasePO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户实体，存储用户基本数据。
 *
 * idx_mobile 唯一索引
 */
@Data
@Accessors(chain = true)
public class UserPO extends BasePO {

    /**
     * 用户编号
     */
    private Long uid;
    /**
     * 手机号
     */
    private String mobile;

}