package cn.iocoder.oceans.coupon.api;

import cn.iocoder.oceans.core.exception.ServiceException;

public interface CouponService {

    /**
     * 校验优惠劵是否可以使用。如果不可以使用，抛出 ServiceException 异常
     *
     * @param couponId 优惠劵编号
     */
    void valid(Long couponId) throws ServiceException;

    /**
     * 使用优惠劵。当使用失败时，抛出 ServiceException 异常。
     *
     * @param couponId 优惠劵编号
     * @param orderId 订单编号。传递该参数的原因是，可以实现幂等
     */
    void use(Long couponId, Long orderId) throws ServiceException;

    /**
     * 归还优惠劵。一般情况下，用于订单取消，或者初始化订单失败时。
     *
     * @param couponId 优惠劵编号
     * @param orderId 订单编号
     */
    void release(Long couponId, Long orderId);

}