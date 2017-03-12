package shohov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.AsyncRestOperations;
import org.springframework.web.client.AsyncRestTemplate;

import java.time.Clock;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class ForFinanceApp {

    public static void main(String[] args) {
        SpringApplication.run(ForFinanceApp.class, args);
    }

    @Bean
    AsyncRestOperations restTemplate() {
        return new AsyncRestTemplate();
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
