package com.example.mungtage.config.multipleDB;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "primaryEntityManager",
        transactionManagerRef = "primaryTransactionManager",
        basePackages = {
                "com.example.mungtage.domain.Lost",
                "com.example.mungtage.domain.Match",
                "com.example.mungtage.domain.User"
        }
)
public class primaryDatabaseConfig {
        @Autowired
        Environment env;

        @Bean
        @Primary
        @ConfigurationProperties(prefix = "spring.datasource.primary")
        public DataSourceProperties primaryDataSourceProperties() {
                return new DataSourceProperties();
        }
        @Bean
        @Primary
        public DataSource primaryDataSource() {
                return primaryDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
        }

        @Bean
        @Primary
        public PlatformTransactionManager primaryTransactionManager() {
                JpaTransactionManager transactionManager = new JpaTransactionManager();
                transactionManager.setEntityManagerFactory(primaryEntityManager().getObject());

                return transactionManager;
        }

        @Bean
        @Primary
        public LocalContainerEntityManagerFactoryBean primaryEntityManager() {
                LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

                em.setDataSource(primaryDataSource());
                em.setPackagesToScan(
                        "com.example.mungtage.domain.Lost.model",
                        "com.example.mungtage.domain.Match.model",
                        "com.example.mungtage.domain.User.model"
                );
                em.setPersistenceUnitName("primaryEntityManager");

                HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
                em.setJpaVendorAdapter(adapter);

                Map<String, Object> properties = new HashMap<>();
                properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
                properties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
                properties.put("hibernate.default_batch_fetch_size", env.getProperty("spring.jpa.properties.hibernate.default_batch_fetch_size"));
                properties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
                em.setJpaPropertyMap(properties);

                return em;
        }
}
