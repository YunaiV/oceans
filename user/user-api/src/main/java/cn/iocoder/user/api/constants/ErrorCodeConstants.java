package cn.iocoder.user.api.constants;

/**
 * 错误码枚举类
 *
 * 用户中心，使用 1-001-000-000 段
 */
public class ErrorCodeConstants {

    // ========== OAUTH2 模块 ==========
    /**
     * OAUTH2 模块 - 未知错误
     */
    public static final int OAUTH2_UNKNOWN = 1001001000;
    /**
     * OAUTH2 模块 - INVALID_GRANT 密码不正确
     */
    public static final int OAUTH2_INVALID_GRANT_BAD_CREDENTIALS = 1001001001;
    /**
     * OAUTH2 模块 - INVALID_GRANT 账号不存在
     */
    public static final int OAUTH2_INVALID_GRANT_USERNAME_NOT_FOUND = 1001001002;
    /**
     * OAUTH2 模块 - INVALID_GRANT 其它
     *
     * 预留 03-09 给这个
     */
    public static final int OAUTH2_INVALID_GRANT = 1001001010;
    /**
     * OAUTH2 模块 - INVALID_TOKEN 访问令牌不存在
     */
    public static final int OAUTH_INVALID_TOKEN_NOT_FOUND = 1001001011;
    /**
     * OAUTH2 模块 - INVALID_TOKEN 访问令牌已过期
     */
    public static final int OAUTH_INVALID_TOKEN_EXPIRED = 1001001012;
    /**
     * OAUTH2 模块 - INVALID_GRANT 其它
     *
     * 预留 13-19 给这个
     */
    public static final int OAUTH_INVALID_TOKEN = 1001001020;
    // TODO 芋艿，剩余的后续翻译
    public static final String INVALID_REQUEST = "invalid_request";
    public static final String INVALID_CLIENT = "invalid_client";
    public static final String UNAUTHORIZED_CLIENT = "unauthorized_client";
    public static final String UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
    public static final String INVALID_SCOPE = "invalid_scope";
    public static final String INSUFFICIENT_SCOPE = "insufficient_scope";
    public static final String REDIRECT_URI_MISMATCH ="redirect_uri_mismatch";
    public static final String UNSUPPORTED_RESPONSE_TYPE ="unsupported_response_type";
    public static final String ACCESS_DENIED = "access_denied";

}
