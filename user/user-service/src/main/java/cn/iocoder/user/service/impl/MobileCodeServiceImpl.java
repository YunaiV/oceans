package cn.iocoder.user.service.impl;

import cn.iocoder.occeans.core.exception.ServiceException;
import cn.iocoder.user.api.MobileCodeService;
import cn.iocoder.user.service.dao.MobileCodeMapper;
import cn.iocoder.user.service.po.MobileCodePO;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Service
//@org.springframework.stereotype.Service("mobileCodeService")
public class MobileCodeServiceImpl implements MobileCodeService {

    @Autowired
    private MobileCodeMapper mobileCodeMapper;

    /**
     * 校验手机号的最后一个手机验证码是否有效
     *
     * @param mobile 手机号
     * @param code 验证码
     * @return 手机验证码信息
     */
    public MobileCodePO validLastMobileCode(String mobile, String code) {
        MobileCodePO mobileCodePO = mobileCodeMapper.selectLast1ByMobile(mobile);
        if (mobileCodePO == null) { // 若验证码不存在，抛出异常
            throw new RuntimeException(""); // TODO 补充
        }
        if (System.currentTimeMillis() - mobileCodePO.getCreateTime().getTime() >= 10 * 60 * 1000) { // 验证码已过期  TODO 芋艿，可配
            throw new RuntimeException(""); // TODO 补充
        }
        if (mobileCodePO.getUsed()) { // 验证码已使用
            throw new RuntimeException(""); // TODO 补充
        }
        if (!mobileCodePO.getCode().equals(code)) {
            throw new RuntimeException(""); // TODO 补充
        }
        return mobileCodePO;
    }

    /**
     * 更新手机验证码已使用
     *
     * @param id 验证码编号
     * @param uid 用户编号
     */
    public void useMobileCode(Long id, Long uid) {
        MobileCodePO update = new MobileCodePO().setId(id).setUsed(true).setUsedUid(uid).setUsedTime(new Date());
        mobileCodeMapper.update(update);
    }

    @Override
    public void send(String mobile) {
        // TODO 芋艿，校验手机是否已经注册
        // 校验是否可以发送验证码
        MobileCodePO lastMobileCodePO = mobileCodeMapper.selectLast1ByMobile(mobile);
        if (lastMobileCodePO != null) {
            if (lastMobileCodePO.getTodayIndex() >= 10) { // 超过当天发送的上限。TODO 需要可配
                throw new ServiceException(-1, "当日发送验证码到达上限");
            }
            if (System.currentTimeMillis() - lastMobileCodePO.getCreateTime().getTime() < 60 * 1000) { // 发送过于频繁
                throw new ServiceException(-1, "验证码发送过于频繁");
            }
        }
        // 创建验证码记录
        MobileCodePO newMobileCodePO = (MobileCodePO) new MobileCodePO().setMobile(mobile)
                .setCode("9999") // TODO 芋艿，随机 4 位验证码 or 6 位验证码
                .setTodayIndex(lastMobileCodePO != null ? lastMobileCodePO.getTodayIndex() : 1)
                .setUsed(false).setCreateTime(new Date());
        mobileCodeMapper.insert(newMobileCodePO);
        // TODO 发送验证码短信
    }

}