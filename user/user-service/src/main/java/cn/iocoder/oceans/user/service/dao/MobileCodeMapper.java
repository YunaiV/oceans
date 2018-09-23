package cn.iocoder.oceans.user.service.dao;

import cn.iocoder.oceans.user.service.po.MobileCodePO;
import org.springframework.stereotype.Repository;

@Repository // 实际不加也没问entity，就是不想 IDEA 那看到有个报错
public interface MobileCodeMapper {

    void insert(MobileCodePO entity);

    /**
     * 更新手机验证码
     *
     * @param entity 更新信息
     */
    void update(MobileCodePO entity);

    /**
     * 获得手机号的最后一个手机验证码
     *
     * @param mobile 手机号
     * @return 手机验证码
     */
    MobileCodePO selectLast1ByMobile(String mobile);

}