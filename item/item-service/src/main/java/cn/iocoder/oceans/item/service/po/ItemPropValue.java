package cn.iocoder.oceans.item.service.po;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品规格值
 */
@Data
@Accessors(chain = true)
public class ItemPropValue {

    /**
     * 值的编号
     */
    private Long valueId;
    /**
     * 所属的键的编号
     */
    private long keyId;
    /**
     * 值的名字
     */
    private String name;
    /**
     * 是否禁用
     */
    private Boolean enabled;

}