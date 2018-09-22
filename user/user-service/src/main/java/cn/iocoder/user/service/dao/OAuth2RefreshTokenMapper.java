package cn.iocoder.user.service.dao;

import cn.iocoder.user.service.po.OAuth2RefreshTokenPO;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2RefreshTokenMapper {

    void insert(OAuth2RefreshTokenPO entity);

}