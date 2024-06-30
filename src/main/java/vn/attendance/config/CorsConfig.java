//package vn.sphinx.hysmart.logistic.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//import vn.sphinx.hysmart.logistic.util.Constants;
//
//import java.util.Arrays;
//
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOriginPattern(CorsConfiguration.ALL);
//        config.addAllowedHeader(CorsConfiguration.ALL);
//        config.addAllowedMethod("*");
//        config.addExposedHeader(Constants.X_TOTAL_COUNT);
//        config.addExposedHeader(Constants.SUGGEST_CODE);
//        config.addExposedHeader(Constants.ERROR_CODE);
//        config.setAllowedOrigins(Arrays.asList("http://localhost:8016"));
//        config.setMaxAge(3600L);
//        config.addAllowedHeader("Requestor-Type");
//        config.addExposedHeader("X-Get-Header");
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsFilter(source);
//    }
//}
