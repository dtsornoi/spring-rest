package com.rest.demo.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@ComponentScan("com.rest.demo")
@EnableWebMvc
@EnableTransactionManagement
@PropertySource({"classpath:persistence-mysql.properties"})
public class AppConfig {

    private Environment environment;

    @Autowired
    public AppConfig(Environment environment){
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource(){
        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        try {
            dataSource.setDriverClass(environment.getProperty("jdbc.driver"));
            dataSource.setJdbcUrl(environment.getProperty("jdbc.url"));
            dataSource.setUser(environment.getProperty("jdbc.user"));
            dataSource.setPassword(environment.getProperty("jdbc.password"));

            dataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
            dataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
            dataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
            dataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));
        } catch (PropertyVetoException exception) {
            throw new RuntimeException(exception);
        }

        return dataSource;
    }

    private int getIntProperty(String propName) {

        String propVal = environment.getProperty(propName);

        return Integer.parseInt(propVal);
    }

    private Properties getHibernateProperties(){
        Properties props = new Properties();

        props.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
        props.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));

        return props;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(){
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(environment.getProperty("hibernate.packagesToScan"));
        sessionFactory.setHibernateProperties(getHibernateProperties());

        return sessionFactory;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {

        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }
}

