package vn.attendance.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class JdbcConfiguration {

    /**
     * định nghĩa bean infoJdbcTemplate cho lần jdbc sau
     *
     * @param dataSource
     * @return
     */
    @Bean
    public JdbcTemplate JdbcTemplate(@Qualifier("datasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
