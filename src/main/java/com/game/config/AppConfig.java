package com.game.config;

import com.game.entity.Player;
import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("com.game")
public class AppConfig {
    @Bean
    @Order(value = 0)
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/rpg_db");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }

    @Bean
    @Order(value = 1)
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        return liquibase;
    }

    @Bean
    @DependsOn("liquibase")
    public SessionFactory sessionFactory(DataSource dataSource) {
        Properties props = new Properties();
        props.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty(Environment.SHOW_SQL, "true");
        props.setProperty(Environment.HBM2DDL_AUTO, "validate");

        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration
                .setProperties(props)
                .addAnnotatedClass(Player.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(props)
                .applySetting(Environment.DATASOURCE, dataSource)
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

}