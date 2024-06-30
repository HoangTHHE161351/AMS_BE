package vn.attendance;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import vn.attendance.config.DateSqlDeserializer;
import vn.attendance.config.DateSqlSerializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Date;

@SpringBootApplication
//@EntityScan(basePackages = "vn.travel.model")
//@EnableJpaRepositories("vn.travel.repository")
@EnableScheduling
@Slf4j
@EnableBatchProcessing
@Configuration
public class AttendenceBeApplication {

    @Value("${rest.connect-timeout}")
    private Duration connectTimeout;
    @Value("${rest.read-timeout}")
    private Duration readTimeout;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AttendenceBeApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String rabbitMqHost = env.getProperty("spring.rabbitmq.host");
        String rabbitMqPort = env.getProperty("spring.rabbitmq.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using localhost as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "server: \t{}://{}:{}{}\n\t" +
                        "swagger: \t {}://localhost:{}{}docs/swagger-ui/index.html?configUrl=/docs/api-docs/swagger-config\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                protocol,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }

    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(connectTimeout == null ? Duration.ofSeconds(5) : connectTimeout)
                .setReadTimeout(readTimeout == null ? Duration.ofSeconds(5) : readTimeout)
                .build();
    }

    @Bean
    public JavaTimeModule javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(Date.class, new DateSqlDeserializer());
        javaTimeModule.addSerializer(Date.class, new DateSqlSerializer());
        javaTimeModule.addDeserializer(Date.class, new DateSqlDeserializer());
        javaTimeModule.addSerializer(Date.class, new DateSqlSerializer());
        return javaTimeModule;
    }
}
