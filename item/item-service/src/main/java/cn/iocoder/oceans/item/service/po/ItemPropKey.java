package cn.iocoder.oceans.item.service.po;

import cn.iocoder.oceans.core.po.BasePO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品规格键
 */
@Data
@Accessors(chain = true)
public class ItemPropKey extends BasePO {

    /**
     * 键的编号
     */
    private Long keyId;
    /**
     * 键的名字
     */
    private String name;
    /**
     * 是否禁用
     */
    private Boolean enabled;

}