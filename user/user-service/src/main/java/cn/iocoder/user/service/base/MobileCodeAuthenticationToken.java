package cn.iocoder.user.service.base;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MobileCodeAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 手机号
     */
    private final Object principal;

    // 未认证的 MobileCodeAuthenticationToken 对象
    public MobileCodeAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
    }

    // 已认证的 MobileCodeAuthenticationToken 对象
    public MobileCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true); // must use super, as we override 标记已认证
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

}