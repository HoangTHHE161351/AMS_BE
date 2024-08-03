package vn.attendance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

@Configuration
@EnableAsync
public class ThreadPoolManager {
    @Autowired
    private Environment env;

    @Bean(name = "IntegrateThreadPoolManager")
    public ThreadPoolExecutorFactoryBean myTaskExecutor() {
        ThreadPoolExecutorFactoryBean threadPoolExecutorFactoryBean = new ThreadPoolExecutorFactoryBean();
        threadPoolExecutorFactoryBean.setCorePoolSize(env.getRequiredProperty("threadpoll.corePoolSize", Integer.class));
        threadPoolExecutorFactoryBean.setMaxPoolSize(env.getRequiredProperty("threadpoll.maxPoolSize", Integer.class));
        threadPoolExecutorFactoryBean.setKeepAliveSeconds(env.getRequiredProperty("threadpoll.keepAliveSeconds", Integer.class));
        threadPoolExecutorFactoryBean.setQueueCapacity(env.getRequiredProperty("threadpoll.queueCapacity", Integer.class));
        threadPoolExecutorFactoryBean.setThreadNamePrefix("NID_ThreadName_Consuming_PackageData: ");
        threadPoolExecutorFactoryBean.setThreadGroupName("NID_Group_ThreadGroupName: ");
        threadPoolExecutorFactoryBean.setAllowCoreThreadTimeOut(env.getRequiredProperty("threadpoll.allowCoreThreadTimeOut", Boolean.class));
        threadPoolExecutorFactoryBean.setWaitForTasksToCompleteOnShutdown(false);
        threadPoolExecutorFactoryBean.setKeepAliveSeconds(30);
        threadPoolExecutorFactoryBean.setDaemon(true);

        threadPoolExecutorFactoryBean.afterPropertiesSet();
        return threadPoolExecutorFactoryBean;
    }
}
