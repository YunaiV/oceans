package cn.iocoder.oceans.item.service.impl;

import cn.iocoder.oceans.item.api.ItemCategoryService;
import cn.iocoder.oceans.item.api.dto.ItemCategoryDTO;
import cn.iocoder.oceans.item.api.dto.ItemCategorySimpleDTO;
import cn.iocoder.oceans.item.service.dao.ItemCategoryMapper;
import cn.iocoder.oceans.item.service.po.ItemCategoryPO;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {

    /**
     * 排序器 —— 按照 Index 升序
     */
    private static final Comparator<ItemCategoryPO> COMPARATOR_INDEX_ASC = (o1, o2) -> o1.getIndex().compareTo(o2.getIndex());

    @Autowired
    private ItemCategoryMapper itemCategoryMapper;

    @Override
    public List<ItemCategorySimpleDTO> getEnabledSimpleList() {
        List<ItemCategoryPO> itemCategoryPOs = itemCategoryMapper.selectListByEnabled(true);
        itemCategoryPOs.sort(COMPARATOR_INDEX_ASC);
        // 转换
        List<ItemCategorySimpleDTO> itemCategoryDTOs = new ArrayList<>(itemCategoryPOs.size());
        itemCategoryPOs.forEach(itemCategoryPO -> itemCategoryDTOs.add(
                new ItemCategorySimpleDTO().setCid(itemCategoryPO.getCid())
                        .setName(itemCategoryPO.getName())
                        .setIndex(itemCategoryPO.getIndex())));
        return itemCategoryDTOs;
    }

    @Override
    public List<ItemCategoryDTO> getList() {
        List<ItemCategoryPO> itemCategoryPOs = itemCategoryMapper.selectListByEnabled(null);
        itemCategoryPOs.sort(COMPARATOR_INDEX_ASC);
        // 转换
        List<ItemCategoryDTO> itemCategoryDTOs = new ArrayList<>(itemCategoryPOs.size());
        itemCategoryPOs.forEach(itemCategoryPO -> itemCategoryDTOs.add(
                new ItemCategoryDTO().setCid(itemCategoryPO.getCid())
                        .setName(itemCategoryPO.getName())
                        .setIndex(itemCategoryPO.getIndex())
                        .setEnabled(itemCategoryPO.getEnabled())
                        .setCreateTime(itemCategoryPO.getCreateTime())));
        return itemCategoryDTOs;
    }

    @Override
    public ItemCategorySimpleDTO getSimple(Long cid) {
        ItemCategoryPO itemCategoryPO = itemCategoryMapper.selectByCid(cid);
        return new ItemCategorySimpleDTO().setCid(itemCategoryPO.getCid())
                .setName(itemCategoryPO.getName())
                .setIndex(itemCategoryPO.getIndex());
    }

}