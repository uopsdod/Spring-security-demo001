package com.spring;



import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.bean.VersionBean;
import com.google.gson.Gson;
import com.util.Util;

@Configuration
@EnableMBeanExport(defaultDomain="${projectName}")
@PropertySource("classpath:application.properties")
@ConfigurationProperties
public class BeanGenerator {
	
	private Map<String, String> mapProp;
	
	public Map<String, String> getMapProp() {
		return mapProp;
	}

	public void setMapProp(Map<String, String> mapProp) {
		this.mapProp = mapProp;
	}
	
	/**
	 * gson instance
	 * @return
	 */
	@Bean
	public Gson gson(){
		return new Gson();
	}
	
	/**
	 * insert this bean into Util constructor, and write current version number into file
	 * @return
	 */
	@Bean(name="VersionBean")
	public VersionBean versionBean(){
		return new VersionBean();
	}
	
	/**
	 * to retrieve beans created in Spring and cope with bean initiation order problem
	 * @return
	 */
	@Bean(name="SpringContextHolder")
	public SpringContextHolder springContextHolder() {
	   return new SpringContextHolder();
	}
	
	
	/**
	 * ApplicationListenerBean starts 
	 * @return
	 */
	@Bean 
	public com.bean.ApplicationListenerBean ApplicationListenerBean(){
		return new com.bean.ApplicationListenerBean();
	}

	
	/**
	 * Util
	 * @return
	 */
	@Bean(name="Util")
	public Util util(Gson aGson, VersionBean aVersionBean){
		Util util = new Util(aGson, aVersionBean);
		Util.getSystemParam().putAll(mapProp);
		Util.getConsoleLogger().info("BeanGenerator - mapProp: " + mapProp);
		return util;
	}
	
}
