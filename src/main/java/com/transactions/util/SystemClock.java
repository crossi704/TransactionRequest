package com.transactions.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.time.Clock;

@Configuration
@EnableScheduling
public class SystemClock {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

}

