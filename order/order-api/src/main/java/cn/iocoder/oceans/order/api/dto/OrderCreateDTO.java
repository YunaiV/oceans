package cn.iocoder.oceans.order.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 订单创建 DTO
 */
@Data
@Accessors(chain = true)
public class OrderCreateDTO implements Serializable {

    /**
     * 优惠劵编号
     */
    private Long couponId;

}
