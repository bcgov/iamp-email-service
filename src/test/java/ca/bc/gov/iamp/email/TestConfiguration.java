package ca.bc.gov.iamp.email;

import ca.bc.gov.iamp.email.api.NotificatorController;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;

@Configuration
@ComponentScan(basePackages = {"ca.bc.gov"}, excludeFilters = {
		//Disable SFTP Component
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ca.bc.gov.file.component.*"),
        //Disable test API
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = NotificatorController.class)} )

// Disable DataSource, JPA and JMX
@EnableAutoConfiguration(exclude = {
		DataSourceAutoConfiguration.class, 
		DataSourceTransactionManagerAutoConfiguration.class, 
		HibernateJpaAutoConfiguration.class,
		JmxAutoConfiguration.class})

@TestPropertySource(locations = "classpath:application-testing.properties")

public class TestConfiguration {

	@MockBean
	JavaMailSender sender;

}
