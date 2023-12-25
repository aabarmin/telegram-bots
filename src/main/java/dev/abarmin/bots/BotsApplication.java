package dev.abarmin.bots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BotsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotsApplication.class, args);
	}

}
