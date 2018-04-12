package com.zhph.config.datasource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.github.pagehelper.PageHelper;

@Configuration
@MapperScan(basePackages = "com.zhph.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

	protected Log log = LogFactory.getLog(DataSourceConfig.class);

	/**
	 * 主数据库
	 * @return
	 */
	@Bean(name = "salaryDS")
	@ConfigurationProperties(prefix = "spring.datasource.salary")
	public DataSource salaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * 业务系统数据源
	 * @return
	 */
	@Bean(name = "busiDS")
	@ConfigurationProperties(prefix = "spring.datasource.busi") 
	public DataSource busiDataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * 动态数据源: 通过AOP在不同数据源之间动态切换
	 * 
	 * @return
	 */
	@Bean(name = "dataSource")
	public DataSource dataSource() {
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		// 默认数据源
		dynamicDataSource.setDefaultTargetDataSource(salaryDataSource());
		// 配置多数据源
		Map<Object, Object> dsMap = new HashMap<Object, Object>(5);
		dsMap.put(DataSourceNameList.SALARY_DATASOURCE.getDatasourceName(), salaryDataSource());
		dsMap.put(DataSourceNameList.BUSI_DATASOURCE.getDatasourceName(), busiDataSource());
		dynamicDataSource.setTargetDataSources(dsMap);
		return dynamicDataSource;
	}

	// 提供SqlSeesion
	@Bean(name = "sqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Interceptor[] plugins = new Interceptor[] { pageHelper() }; // 重新配置了sessionFactory后
		// 需要重新陪着pagehelper
		sqlSessionFactoryBean.setPlugins(plugins);
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/mapper/*/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	/**
	 * 
	 * @Title pageHelper
	 * @Description 配置mybatis的分页插件
	 * @param  参数
	 * @return PageHelper 返回类型
	 */
	@Bean(name = "PageHelper")
	public PageHelper pageHelper() {
		System.out.println("MyBatisPageHelperConfiguration.pageHelper()");
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		pageHelper.setProperties(p);
		return pageHelper;
	}

	@Bean(name = "transactionManager")
	@Primary
	public PlatformTransactionManager transactionManager() throws SQLException {
		return new DataSourceTransactionManager(dataSource());
	}
}

class DynamicDataSource extends AbstractRoutingDataSource {
	private static final Log log = LogFactory.getLog(DataSource.class);
	@Override
	protected Object determineCurrentLookupKey() {
		log.debug("数据源为{" + DynamicDataSourceContextHolder.getDB() + "}");
		return DynamicDataSourceContextHolder.getDB();
	}
}


