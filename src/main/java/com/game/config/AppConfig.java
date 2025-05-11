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
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("com.game")
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        String dbHost = System.getenv().getOrDefault("DB_HOST", "localhost");
        String dbPort = System.getenv().getOrDefault("DB_PORT", "5432");
        String dbName = System.getenv().getOrDefault("DB_NAME", "rpg_db");
        String dbUser = System.getenv().getOrDefault("DB_USER", "postgres");
        String dbPass = System.getenv().getOrDefault("DB_PASS", "postgres");

        String url = "jdbc:p6spy:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.p6spy.engine.spy.P6SpyDriver");
        dataSource.setUrl(url);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPass);

        return dataSource;
    }

    @Bean
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