package cn.iocoder.oceans.item.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * ItemCategory DTO
 */
@Data
@Accessors(chain = true)
public class ItemCategoryDTO implements Serializable {

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
    /**
     * 创建时间
     */
    private Date createTime;

}