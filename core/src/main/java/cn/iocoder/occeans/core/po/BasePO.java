package cn.iocoder.occeans.core.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * PO 基类
 */
@Data
@Accessors(chain = true)
public abstract class BasePO {

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

}
