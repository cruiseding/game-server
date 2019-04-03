package com.jjy.game.manage.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.jjy.game.manage.interceptor.UserInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan("com.jjy.game.manage.controller")
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getUserInterceptor()).addPathPatterns("/server/**", "/gm/**");
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
	
	@Bean
	public UserInterceptor getUserInterceptor() {
		return new UserInterceptor();
	}
	
	@Bean 
	public FreeMarkerViewResolver getFreemarkerViewResolver() {
		return new FreeMarkerViewResolver();
	}

}
