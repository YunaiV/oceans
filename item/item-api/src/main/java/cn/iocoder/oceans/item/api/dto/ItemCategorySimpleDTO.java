package cn.iocoder.oceans.item.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ItemCategory 简化 DTO
 */
@Data
@Accessors(chain = true)
public class ItemCategorySimpleDTO implements Serializable {

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

}
