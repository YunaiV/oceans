package cn.iocoder.oceans.order.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 订单简化信息 DTO
 */
@Data
@Accessors(chain = true)
public class OrderSimpleDTO {

    /**
     * 订单编号
     */
    private Long orderId;

}
