package vn.attendance.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;

/**
 *  The class for flyway configuration.
 *
 */
@Component
public class FlywayConfig implements FlywayMigrationStrategy {

    private static final Logger logger = LoggerFactory.getLogger(FlywayConfig.class);

    @Value("${app.flyway.repair}")
    private boolean isFlywayRepair;

    @Override
    public void migrate(Flyway flyway) {
        if (this.isFlywayRepair) {
            logger.info("-----Running Flyway Repair.");
            flyway.repair();
        } else {
            logger.info("-----Running Flyway Migration.");
            flyway.migrate();
        }
    }
}