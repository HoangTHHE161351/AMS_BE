package vn.attendance.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class JasyptAdvancedConfig {
    @Value("${jasypt.encryptor.algorithm}")
    private String algorithm;

    @Value("${jasypt.encryptor.key-obtention-iterations}")
    private String keyObtentions;

    @Value("${jasypt.encryptor.pool-size}")
    private String pollSize;

    @Value("${jasypt.encryptor.provider-name}")
    private String providerName;

    @Value("${jasypt.encryptor.salt-generator-classname}")
    private String saltGeneratorClassname;

    @Value("${jasypt.encryptor.string-output-type}")
    private String outputType;

    @Value("${jasypt.encryptor.password}")
    private String password;

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor getPasswordEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(decode(password));

        config.setAlgorithm(algorithm);
        config.setKeyObtentionIterations(keyObtentions);
        config.setPoolSize(pollSize);
        config.setProviderName(providerName);
        config.setSaltGeneratorClassName(saltGeneratorClassname);
        config.setStringOutputType(outputType);

        encryptor.setConfig(config);
        return encryptor;
    }

    /***
     * Decode Password
     */
    private String decode(String decode) {
        decode = new String(Base64.getDecoder().decode(decode));
        return decode;
    }
}
