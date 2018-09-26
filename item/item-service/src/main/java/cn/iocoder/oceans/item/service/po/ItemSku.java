package cn.iocoder.oceans.item.service.po;

import cn.iocoder.oceans.core.po.BasePO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 商品 SKU
 */
@Data
@Accessors(chain = true)
public class ItemSku extends BasePO {

    @Data
    @Accessors(chain = true)
    class Property {

        /**
         * 规格键的编号
         */
        private Long keyId;
        /**
         * 规格键的名字
         */
        private String keyName;
        /**
         * 规格值的名字
         */
        private Long valueId;
        /**
         * 规格值的名字
         */
        private String valueName;

    }

    /**
     * Sku 编号
     */
    private Long skuId;
    /**
     * 商品编号
     */
    private Long itemId;
    /**
     * 图片地址
     */
    private String imageURL;
    /**
     * 商品规格字符串
     *
     * {@link #properties} 进行 json 成字符串
     */
    private String propertiesStr;
    /**
     * 商品规格数组
     */
    private List<Property> properties;
    /**
     * 价格，单位：分
     */
    private Integer price;
    /**
     * 库存数量
     */
    private Integer quantity;
    /**
     * 销量
     */
    private Integer soldNum;

}