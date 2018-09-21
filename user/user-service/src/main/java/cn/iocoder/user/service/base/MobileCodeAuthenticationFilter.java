package cn.iocoder.user.service.base;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MobileCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private boolean postOnly = true;

    protected MobileCodeAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(new AntPathRequestMatcher("/mobile/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 若仅允许 POST 请求，则校验。不通过，则抛出 AuthenticationServiceException 异常
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // 获得参数
        String mobile = request.getParameter("mobile");
        mobile = mobile != null ? mobile.trim() : "";

        // 创建 MobileCodeAuthenticationToken 对象，即授权请求
        MobileCodeAuthenticationToken authRequest = new MobileCodeAuthenticationToken(mobile);

        //设置用户信息
        setDetails(request, authRequest);

        //返回Authentication实例
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    // TODO 芋艿，这个在干啥
    protected void setDetails(HttpServletRequest request, MobileCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}
