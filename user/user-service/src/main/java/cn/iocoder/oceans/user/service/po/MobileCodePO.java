package cn.iocoder.oceans.user.service.po;

import cn.iocoder.oceans.core.po.BasePO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

// TODO 优化，IP
@Data
@Accessors(chain = true)
public class MobileCodePO extends BasePO {

    /**
     * 编号
     */
    private Long id;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 验证码
     */
    private String code;
    /**
     * 今日发送的第几条
     */
    private Integer todayIndex;
    /**
     * 是否使用
     */
    private Boolean used;
    /**
     * 注册的用户编号
     */
    private Long usedUid;
    /**
     * 使用时间
     */
    private Date usedTime;

}