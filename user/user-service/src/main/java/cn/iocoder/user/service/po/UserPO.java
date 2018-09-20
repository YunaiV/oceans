package cn.iocoder.user.service.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体，存储用户基本数据。
 */
@Data
@EqualsAndHashCode
public class UserPO {

    /**
     * 用户编号
     */
    private Long uid;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码的盐
     */
    private String passowrdSalt;

}