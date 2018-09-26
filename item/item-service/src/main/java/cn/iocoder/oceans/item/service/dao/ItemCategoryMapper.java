package cn.iocoder.oceans.item.service.dao;

import cn.iocoder.oceans.item.service.po.ItemCategoryPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCategoryMapper {

    /**
     * 获得分类数组
     *
     * @param enabled 是否开启
     * @return 分类数组
     */
    List<ItemCategoryPO> selectListByEnabled(@Param("enabled") Boolean enabled);

    /**
     * 获得单个分类
     *
     * @param cid 分类编号
     * @return 分类
     */
    ItemCategoryPO selectByCid(Long cid);

}