package cn.iocoder.user.api;

import cn.iocoder.occeans.core.exception.ServiceException;

public interface MobileCodeService {

    /**
     * 发送验证码
     *
     * @param mobile 手机号
     */
    void send(String mobile) throws ServiceException;

}
