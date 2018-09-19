package cn.iocoder.webapp.bff.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;

/**
 * OAuth2 Resource 配置
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceConfiguration extends ResourceServerConfigurerAdapter {

//    @Override
//    public void configure(final HttpSecurity http) throws Exception {
//        // @formatter:off
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .and()
//                .authorizeRequests().anyRequest().permitAll();
//        // @formatter:on
//    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .antMatchers("/passport/**").permitAll()
//                .antMatchers("/oauth/token/revokeById/**").permitAll()
//                .antMatchers("/tokens/**").permitAll()
//                .antMatchers("/oauth/**").permitAll()
                .anyRequest().authenticated()
//                .and().formLogin().permitAll()
                .and().csrf().disable();
        // @formatter:on
    }

    @Primary
    @Bean
    public RemoteTokenServices tokenServices(RestTemplate restTemplate) {
        final RemoteTokenServices tokenService = new RemoteTokenServices();
        // TODO 芋艿 考虑接入 dubbo 实现
//        tokenService.setCheckTokenEndpointUrl("http://localhost:8081/oauth/check_token");
        tokenService.setCheckTokenEndpointUrl("http://user-service/oauth/check_token");
        // TODO 芋艿，配置中心
        tokenService.setClientId("fooClientIdPassword");
        tokenService.setClientSecret("secret");
        tokenService.setRestTemplate(restTemplate);
        return tokenService;
    }

    @Bean
    @LoadBalanced // TODO 芋艿，需要移动到别处
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
