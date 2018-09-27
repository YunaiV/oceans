package cn.iocoder.oceans.coupon.service.po;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 优惠劵 PO
 */
@Data
@Accessors(chain = true)
public class CouponPO {

    /**
     * 优惠劵
     */
    private Long couponId;
    /**
     * 用户编号
     */
    private Integer uid;
    /**
     * 是否有效
     */
    // TODO 芋艿，未来在考虑是否过期之类的、已经使用
    private Boolean valid;

}