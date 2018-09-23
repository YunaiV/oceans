package cn.iocoder.oceans.user.service.dao;

import cn.iocoder.oceans.user.service.po.UserRegisterPO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegisterMapper {

    void insert(UserRegisterPO entity);

}