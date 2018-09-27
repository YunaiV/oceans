package cn.iocoder.oceans.order.service.dao;

import cn.iocoder.oceans.order.service.po.OrderPO;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {

    void insert(OrderPO entity);

    void update(OrderPO update);

}