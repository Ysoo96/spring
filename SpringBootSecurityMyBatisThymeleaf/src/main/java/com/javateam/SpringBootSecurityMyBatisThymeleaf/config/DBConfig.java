package com.javateam.SpringBootSecurityMyBatisThymeleaf.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DBConfig {
	
	@Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSourceProperties dataSourceProp() {
        return new DataSourceProperties();
    }
	
    @Primary
    @Bean(name="dataSource")
    @ConfigurationProperties(prefix="spring.datasource")
    // @Qualifier("primary")
    public DataSource dataSource() {
        return dataSourceProp().initializeDataSourceBuilder().build();
    }

    @Bean(name="hikariCP")
    // @Primary
    @ConfigurationProperties(prefix="spring.datasource.hikari") 
    public DataSource hikariDataSource() {
        return DataSourceBuilder.create()
        			.type(HikariDataSource.class)
        			.build();
    }

//    @Bean
//	public SqlSessionFactory sqlSessionFactory() throws Exception {
//		
//	    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//	    // factoryBean.setDataSource(hikariDataSource());
//	    factoryBean.setDataSource(dataSource());
//	    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//					.getResources("classpath:/mapper/*.xml"));
//	    
//	    return factoryBean.getObject();
//	}
	
//	@Bean(name="sqlSession")	
//	public SqlSessionTemplate sqlSessionTemplate() throws Exception {
//		
//		return new SqlSessionTemplate(sqlSessionFactory());
//	}
	
	@Bean
    public PlatformTransactionManager getTransactionManager() {
	        
		return new DataSourceTransactionManager(this.hikariDataSource());
	}
	
//	@Bean
//	public DataSourceTransactionManager getTransactionManager2() {
//		
//		return new DataSourceTransactionManager(this.hikariDataSource());
//	} //
	
}
