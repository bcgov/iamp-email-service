package ca.bc.gov.iamp.email;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@ComponentScan(basePackages = {"ca.bc.gov"}, excludeFilters = {} )
@EnableAutoConfiguration(exclude = {})
@TestPropertySource(locations = "classpath:application-testing.properties")
public class TestConfiguration {
}
