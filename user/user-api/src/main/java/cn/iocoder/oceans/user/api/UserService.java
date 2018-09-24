package cn.iocoder.oceans.user.api;

import cn.iocoder.oceans.core.exception.ServiceException;
import cn.iocoder.oceans.user.api.dto.UserDTO;

public interface UserService {

    /**
     * 创建用户。一般在用户注册时，调用该方法
     *
     * @param mobile 手机号
     * @param code 手机验证码
     * @return 用户
     */
    UserDTO createUser(String mobile, String code) throws ServiceException;

}