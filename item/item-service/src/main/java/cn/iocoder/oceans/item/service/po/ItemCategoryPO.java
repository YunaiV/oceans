package cn.iocoder.oceans.item.service.po;

import cn.iocoder.oceans.core.po.BasePO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品分类
 */
@Data
@Accessors(chain = true)
public class ItemCategoryPO extends BasePO {

    /**
     * 分类编号
     */
    private Integer cid;
    /**
     * 名字
     */
    private String name;
    /**
     * 排序
     */
    private Integer index;
    /**
     * 是否开启
     */
    private Boolean enabled;

}