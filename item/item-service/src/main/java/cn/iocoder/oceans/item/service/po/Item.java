package cn.iocoder.oceans.item.service.po;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品
 */
@Data
@Accessors(chain = true)
public class Item {

    /**
     * 商品编号
     */
    private Long itemId;
    /**
     * 是否上架
     */
    private Boolean enabled;

    // ========== 基本信息 BEGIN ==========

    /**
     * 商品标题
     *
     * 不能超过100字，受违禁词控制
     */
    private String title;
    /**
     * 副标题，分享链接时显示
     */
    private String summary;
    /**
     * 商品分类编号
     */
    private Integer cid;
    /**
     * 商品主图地址
     *
     * 数组，以逗号分隔
     *
     * 建议尺寸：800*800像素，你可以拖拽图片调整顺序，最多上传15张
     */
    private String picURLs;

    // ========== 基本信息 END ==========

    // ========== 价格库存 BEGIN ==========

    /**
     * 价格，单位分
     */
    private Integer price;
    /**
     * 总库存
     *
     * 基于 sku 的库存数量累加
     */
    private Integer quantity;
    /**
     * 总销量
     */
    private Integer soldNum;

    // ========== 价格库存 END ==========

    // ========== 运费信息 BEGIN ==========
    // TODO 芋艿，后续补全
    // ========== 运费信息 END ==========

    // TODO 芋艿，snapshotId 商品快照编号
}
