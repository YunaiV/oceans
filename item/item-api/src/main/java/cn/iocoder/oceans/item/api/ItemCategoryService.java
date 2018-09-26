package cn.iocoder.oceans.item.api;

import cn.iocoder.oceans.item.api.dto.ItemCategoryDTO;
import cn.iocoder.oceans.item.api.dto.ItemCategorySimpleDTO;

import java.util.List;

public interface ItemCategoryService {

    /**
     * 获得商品分类数组，不包含禁用的。一般情况下，适用前台项目。
     *
     * 默认情况下，按照分类的 index 升序
     *
     * @return ItemCategoryDTO 数组。每个 ItemCategoryDTO 对象，不包含 enabled 属性
     */
    List<ItemCategorySimpleDTO> getEnabledSimpleList();

    /**
     * 获得商品分类数组，包含禁用的。一般情况下，适用管理后台
     *
     * 默认情况下，按照分类的 index 升序
     *
     * @return ItemCategoryDTO 数组。每个 ItemCategoryDTO 对象，不包含 enabled 属性
     */
    List<ItemCategoryDTO> getList();

    /**
     * 根据分类编号，获得商品分类
     *
     * @param cid 商品分类编号
     * @return 商品分类信息
     */
    ItemCategorySimpleDTO getSimple(Long cid);

}