package com.nowcoder.configuration;

import com.nowcoder.interceptor.LoginRequredInterceptor;
import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//WebMvcConfigurerAdapter已经过时，官方推荐下面的方式
@Component
public class WendaWebConfiguration implements WebMvcConfigurer {

    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequredInterceptor loginRequredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequredInterceptor).addPathPatterns("/user/*");//此拦截器用到hostHolder所以一定要放在上面拦截器的后面，因为是在上面拦截器里面设置的hostHolder
    }
}
