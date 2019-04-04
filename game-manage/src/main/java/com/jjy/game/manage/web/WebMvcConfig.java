package com.jjy.game.manage.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.UrlBasedViewResolverRegistration;
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
	
	@Bean
	public UserInterceptor userInterceptor() {
		return new UserInterceptor();
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		UrlBasedViewResolverRegistration registration = registry.freeMarker();
		registration.prefix("/page/").suffix(".html");
		registration.cache(true);
		registration.viewClass(FreeMarkerView.class);
	}
	
    @Bean
    public MultipartResolver multipartResolver(){
	     CommonsMultipartResolver resolver = new CommonsMultipartResolver();
	     resolver.setDefaultEncoding("UTF-8");
	     resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
	     resolver.setMaxInMemorySize(4096);
	     resolver.setMaxUploadSize(104857600);//上传文件大小 50M 50*1024*1024
	     return resolver;
    }   
    
}
