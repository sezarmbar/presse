package de.stadt.presse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PresseApplication {

	public static void main(String[] args) {
		SpringApplication.run(PresseApplication.class, args);
	}
}
