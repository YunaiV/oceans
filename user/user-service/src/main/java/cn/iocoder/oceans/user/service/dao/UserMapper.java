package cn.iocoder.oceans.user.service.dao;

import cn.iocoder.oceans.user.service.po.UserPO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    void insert(UserPO entity);

    UserPO selectByMobile(String mobile);

}