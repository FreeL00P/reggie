package com.itheima.reggie.config;


import com.itheima.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * WebMvcConfig
 *
 * @author fj
 * @date 2022/10/4 18:15
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
//    @Resource
//    private LoginCheckInterceptor loginCheckInterceptor;
    /**
     * 配置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射");
        registry.addResourceHandler("/backend/**")
                .addResourceLocations("classpath:/backend/**");
        registry.addResourceHandler("/front/**")
                .addResourceLocations("classpath:/front/**");
    }

    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        //创建消息转化器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转化器，底层使用Jackson将java对象转换为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的转换器对象追加到MVC框架的转换器集合中
        converters.add(0,messageConverter);

    }
    //    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginCheckInterceptor).excludePathPatterns("/employee/login","/login",
//                                                        "/employee/logout","/backend/**","/front/**");
//    }
}
