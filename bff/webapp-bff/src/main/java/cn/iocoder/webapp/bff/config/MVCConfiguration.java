package cn.iocoder.webapp.bff.config;

import cn.iocoder.webapp.bff.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@EnableWebMvc
@Configuration
public class MVCConfiguration extends WebMvcConfigurerAdapter {

//    @Bean
//    public MappingJackson2HttpMessageConverter messageConverter() {
//        return new MappingJackson2HttpMessageConverter();
//    }

    @Autowired
    private SecurityInterceptor securityInterceptor;

//    @Reference
//    private OAuth2Service oauth2Service;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
        super.configureMessageConverters(converters);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor);
    }

}