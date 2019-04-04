package com.jjy.game.manage.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import com.jjy.game.manage.interceptor.UserInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan("com.jjy.game.manage.controller")
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private UserInterceptor userInterceptor;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userInterceptor).addPathPatterns("/server/**", "/gm/**");
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("cache", true);   //模板后缀，指定html页面为模板
		attributes.put("suffix", ".html");  //使用这个模板类来解析视图
		attributes.put("viewClass", FreeMarkerView.class.getName()); 
		attributes.put("exposeSpringMacroHelpers", true); 
		attributes.put("exposeRequestAttributes", true); //允许访问请求属性，默认为false
		attributes.put("exposeSessionAttributes", true); //允许访问会话属性，默认为false
		attributes.put("requestContextAttribute", "rc"); //页面上下文，类似于request.contextPath
		attributes.put("contentType", "text/html;charset=UTF-8"); //模板输出内容编码，此处应与defaultEncoding保持一致
		registry.freeMarker().attributes(attributes);
	}
	
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(){
	     CommonsMultipartResolver resolver = new CommonsMultipartResolver();
	     resolver.setDefaultEncoding("UTF-8");
	     resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
	     resolver.setMaxInMemorySize(4096);
	     resolver.setMaxUploadSize(104857600);//上传文件大小 50M 50*1024*1024
	     return resolver;
    }   
	
}
