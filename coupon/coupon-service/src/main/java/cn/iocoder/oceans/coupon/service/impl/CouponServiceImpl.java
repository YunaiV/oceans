package cn.iocoder.oceans.coupon.service.impl;

import cn.iocoder.oceans.core.exception.ServiceException;
import cn.iocoder.oceans.coupon.api.CouponService;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class CouponServiceImpl implements CouponService {

    @Override
    public void valid(Long couponId) throws ServiceException {

    }

    @Override
    public void use(Long couponId, Long orderId) throws ServiceException {

    }

}