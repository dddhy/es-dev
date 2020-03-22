package com.jk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//声明配置类
@Configuration
public class CorsMapping implements WebMvcConfigurer {
    @Override
    /**
     * 重新跨域支持方法
     * CorsRegistry  开启跨域注册
     */
    public void addCorsMappings(CorsRegistry registry) {
        //addMapping 添加可跨域的请求地址
        registry.addMapping("/**")
                //设置跨域 域名权限 规定由某一个指定的域名+端口能访问跨域项目
                //.allowedOrigins("localhost:80")
                .allowedOrigins("*")
                //是否开启cookie跨域  是否支持传cookie
                .allowCredentials(false)
                //规定能够跨域访问的方法类型
                //"OPTIONS" 在访问前面请求时，先发送一个"OPTIONS"请求，验证发送请求的IP是否是.allowedOrigins指定的
                //如果不通过，不会再调用 GET","POST"...方法
                .allowedMethods("GET","POST","DELETE","PUT","OPTIONS")
                //添加验证头信息  token
                .allowedHeaders("*")
                //预检请求存活时间 在此期间不再次发送预检请求 3600毫秒
                .maxAge(3600);
    }
}
