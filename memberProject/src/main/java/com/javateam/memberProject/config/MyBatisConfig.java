package com.javateam.memberProject.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class MyBatisConfig {
	@Bean // XML 설정시 <bean> 의존성 정보에 해당
	// application.properties ==> spring.datasource.* 속성 값(들)
	@ConfigurationProperties(prefix="spring.datasource.hikari")
	public DataSource getDataSource() {
		return DataSourceBuilder.create()
								// .type(DataSource.class)
								.type(HikariDataSource.class)
								.build();
	}
	
	@Bean
	// public DataSourceTransactionManager getTransactionManager() {
	public PlatformTransactionManager getTransactionManager() {
		return new DataSourceTransactionManager(this.getDataSource());
	}
}
