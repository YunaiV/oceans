package cn.iocoder.user.service.config;

import cn.iocoder.user.service.base.AuthorizationExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * OAuth2 Authorization 配置
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private Environment env;

    // 用户认证
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()) // 设置 tokenStore
                .authenticationManager(authenticationManager) // 设置 authenticationManager
                .exceptionTranslator(new AuthorizationExceptionTranslator()); // 自定义 Exception 转换器
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 加载 ClientDetails
        clients.jdbc(dataSource());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        // 设置 TokenKeyEndpoint[ oauth/token_key ]的访问权限，全部允许。用于使用 JWT 作为 token 的场景。
        // 实际我们目前场景下，可以不配置
        oauthServer.tokenKeyAccess("permitAll()")
                // 设置 CheckTokenEndpoint[ oauth/check_token ]的访问权限，验证 client_id + client_secret 正确
                .checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));
        return dataSource;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource());
    }

}