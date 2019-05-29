package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/*
*@author yaqiwe
*@data 2019-05-21 09:14
*@notes 映射本地资源路径
**/
@Configuration
public class fileConfiguration implements WebMvcConfigurer {
    @Value("${fileSrc}")
    String fileSrc;
    @Value("${mappingUrl}")
    String mappingUrl;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceHandler映射的访问路径
        //addResourceLocations 本地资源的绝对路径
        registry.addResourceHandler(mappingUrl+"**").addResourceLocations("file:"+fileSrc);
    }
}
