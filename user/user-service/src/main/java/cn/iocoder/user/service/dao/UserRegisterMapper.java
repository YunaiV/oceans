package cn.iocoder.user.service.dao;

import cn.iocoder.user.service.po.UserRegisterPO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegisterMapper {

    void insert(UserRegisterPO entity);

}