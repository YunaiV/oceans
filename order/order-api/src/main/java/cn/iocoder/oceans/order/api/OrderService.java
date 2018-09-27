package cn.iocoder.oceans.order.api;

import cn.iocoder.oceans.order.api.dto.OrderCreateDTO;

/**
 * 订单 Service
 */
public interface OrderService {

    // TODO 芋艿，要做改造
    void createOrder(OrderCreateDTO orderCreateDTO) throws Exception;

}