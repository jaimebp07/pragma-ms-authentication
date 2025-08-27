package co.com.mycompany;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import co.com.mycompany.r2dbc.config.PostgresqlConnectionProperties;

@SpringBootApplication(scanBasePackages = "co.com.mycompany")
@ConfigurationPropertiesScan
@EnableConfigurationProperties({ PostgresqlConnectionProperties.class })
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
