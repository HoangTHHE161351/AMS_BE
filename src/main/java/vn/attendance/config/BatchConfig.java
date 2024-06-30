package vn.attendance.config;

import lombok.SneakyThrows;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig {
    @SneakyThrows
    @Bean
    public JobRepository getJobRepository(DataSource dataSource) {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
        factoryBean.setTransactionManager(new ResourcelessTransactionManager());
        factoryBean.setDataSource(dataSource);
        factoryBean.afterPropertiesSet();
        factoryBean.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
        return factoryBean.getObject();
    }

    @Bean
    SimpleJobLauncher powerJobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

//    @Bean
//    public LockProvider
}
