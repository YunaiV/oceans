package cn.iocoder.oceans.user.service.dao;

import cn.iocoder.oceans.user.service.po.OAuth2RefreshTokenPO;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2RefreshTokenMapper {

    void insert(OAuth2RefreshTokenPO entity);

}