package cz.mares.zonkyprinter;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("prod")
@Configuration
@EnableScheduling
public class ZonkyPrintSchedulingConfig {
}
