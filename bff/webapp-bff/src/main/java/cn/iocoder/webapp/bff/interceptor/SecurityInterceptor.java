package cn.iocoder.webapp.bff.interceptor;

import cn.iocoder.occeans.core.exception.ServiceException;
import cn.iocoder.oceans.user.api.OAuth2Service;
import cn.iocoder.oceans.user.api.dto.OAuth2AuthenticationDTO;
import cn.iocoder.webapp.bff.annotation.PermitAll;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 安全拦截器
 */
@Component
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    @Reference
    private OAuth2Service oauth2Service;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 校验访问令牌是否正确。若正确，返回授权信息
        String accessToken = obtainAccess(request);
        OAuth2AuthenticationDTO authentication = null;
        if (accessToken != null) {
            authentication = oauth2Service.checkToken(accessToken);
        }
        // 校验是否需要已授权
        HandlerMethod method = (HandlerMethod) handler;
        boolean isPermitAll = method.hasMethodAnnotation(PermitAll.class);
        if (!isPermitAll && authentication == null) {
            throw new ServiceException(-1, "未授权");
        }
        return super.preHandle(request, response, handler);
    }

    private String obtainAccess(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorization)) {
            return null;
        }
        int index = authorization.indexOf("Bearer ");
        if (index == -1) { // 未找到
            return null;
        }
        return authorization.substring(index + 7).trim();
    }

}
