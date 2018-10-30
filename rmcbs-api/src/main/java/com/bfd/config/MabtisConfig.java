package com.bfd.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;

/**
 * MabtisConfig
 * 
 * @author xile.lu
 * @version [版本号, 2018年7月31日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Configuration
@MapperScan(basePackages = "com.bfd.dao.mapper", sqlSessionTemplateRef = "druidSqlSessionTemplate")
public class MabtisConfig {
    
    static final String PACKAGE = "com.bfd.bean";
    
    static final String MAPPER_LOCATION = "classpath:mapper/*.xml";
    
    @Value("${spring.datasource.druid.url}")
    private String url;
    
    @Value("${spring.datasource.druid.username}")
    private String user;
    
    @Value("${spring.datasource.druid.password}")
    private String password;
    
    @Value("${spring.datasource.druid.driver-class-name}")
    private String driverClass;
    
    @Value("${spring.datasource.druid.initialSize}")
    private int initialSize;
    
    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;
    
    @Value("${spring.datasource.druid.maxActive}")
    private int maxActive;
    
    @Value("${spring.datasource.druid.maxWait}")
    private long maxWait;
    
    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;
    
    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;
    
    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;
    
    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;
    
    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;
    
    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;
    
    @Value("${spring.datasource.druid.filters}")
    private String filters;
    
    @Bean(name = "druidDataSource", destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    @Primary
    public DataSource druidDataSource(DataSourceProperties properties) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        
        //
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        
        dataSource.setFilters(filters);
        
        // return DataSourceBuilder.create().type(dataSource.getClass()).build();
        return dataSource;
    }
    
    @Bean(name = "druidSqlSessionFactory")
    @Primary
    public SqlSessionFactory druidSqlSessionFactory(@Qualifier("druidDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage(PACKAGE);
        
        // 分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "false");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        
        // 添加插件
        bean.setPlugins(new Interceptor[] {pageHelper});
        
        // 添加XML目录
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return bean.getObject();
    }
    
    @Bean(name = "druidTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("druidDataSource") DataSource druidDataSource) {
        return new DataSourceTransactionManager(druidDataSource);
    }
    
    @Bean(name = "druidSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("druidSqlSessionFactory") SqlSessionFactory druidSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(druidSqlSessionFactory);
    }
    
}
