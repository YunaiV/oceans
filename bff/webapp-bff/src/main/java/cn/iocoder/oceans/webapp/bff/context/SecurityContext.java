package cn.iocoder.oceans.webapp.bff.context;

import lombok.Data;

/**
 * Security 上下文
 */
@Data
public class SecurityContext {

    private final Long uid;

    public SecurityContext(Long uid) {
        this.uid = uid;
    }

}