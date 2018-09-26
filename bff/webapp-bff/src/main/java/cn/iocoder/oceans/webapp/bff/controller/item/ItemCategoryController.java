package cn.iocoder.oceans.webapp.bff.controller.item;

import cn.iocoder.oceans.item.api.ItemCategoryService;
import cn.iocoder.oceans.item.api.dto.ItemCategorySimpleDTO;
import cn.iocoder.oceans.webapp.bff.annotation.PermitAll;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item_category")
public class ItemCategoryController {

    @Reference
    private ItemCategoryService itemCategoryService;

    @PermitAll
    @GetMapping("/list")
    public List<ItemCategorySimpleDTO> list() {
        return itemCategoryService.getEnabledSimpleList();
    }

}