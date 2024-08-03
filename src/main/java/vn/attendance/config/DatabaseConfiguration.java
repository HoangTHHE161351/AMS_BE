package vn.attendance.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = {
                "vn.attendance"
        }
)
public class DatabaseConfiguration {
    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Autowired
    private Environment env;

    @Primary
    @Bean(name = "datasource")
    public DataSource dataSource() {
        if (env.getProperty("spring.datasource.url") == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                            " cannot start. Please check your Spring profile, current profiles are: {}",
                    Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        config.setJdbcUrl(env.getProperty("spring.datasource.url"));
        if (env.getProperty("spring.datasource.username") != null) {
            config.addDataSourceProperty("user", env.getProperty("spring.datasource.username"));
        } else {
            config.addDataSourceProperty("user", ""); // HikariCP doesn't allow null user
        }
        if (env.getProperty("spring.datasource.password") != null) {
            config.addDataSourceProperty("password", env.getProperty("spring.datasource.password"));
        } else {
            config.addDataSourceProperty("password", ""); // HikariCP doesn't allow null password
        }
        config.setMaximumPoolSize(env.getProperty("spring.datasource.maximum-pool-size", Integer.class));
        config.setMaxLifetime(env.getProperty("spring.datasource.max-lifetime", Long.class));
        config.setConnectionTimeout(env.getProperty("spring.datasource.connection-timeout", Long.class));
        config.setMinimumIdle(env.getProperty("spring.datasource.minimum-idle", Integer.class));
        config.setIdleTimeout(env.getProperty("spring.datasource.idle-timeout", Long.class));
        config.setAllowPoolSuspension(false);
        config.setPoolName(env.getProperty("spring.datasource.pool-name"));
        config.setLeakDetectionThreshold(0);
        config.setAutoCommit(false);
        return new HikariDataSource(config);
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("datasource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("vn.attendance.model")
                .persistenceUnit("persistenUnitName")
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
