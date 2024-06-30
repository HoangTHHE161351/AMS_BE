package vn.attendance.config;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    @Qualifier("jwtConfigAuth")
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

}
