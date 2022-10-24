package com.example.mungtage.config.multipleDB;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "secondEntityManager",
        transactionManagerRef = "secondTransactionManager",
        basePackages = {
                "com.example.mungtage.domain.Rescue"
        }
)
public class secondDatabaseConfig {
        @Autowired
        Environment env;

        @Bean
        @ConfigurationProperties(prefix = "spring.datasource.second")
        public DataSourceProperties secondDataSourceProperties() {
                return new DataSourceProperties();
        }

        @Bean
        public DataSource secondDataSource() {
                return secondDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
        }

        @Bean
        public PlatformTransactionManager secondTransactionManager() {
                JpaTransactionManager transactionManager = new JpaTransactionManager();
                transactionManager.setEntityManagerFactory(secondEntityManager().getObject());

                return transactionManager;
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean secondEntityManager() {
                LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
                em.setDataSource(secondDataSource());
                em.setPackagesToScan("com.example.mungtage.domain.Rescue.model");

                HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
                em.setJpaVendorAdapter(adapter);

                Map<String, Object> properties = new HashMap<>();
                properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
                em.setJpaPropertyMap(properties);

                return em;
        }
}
