package org.zerock.b02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = "org.zerock.b02")
public class  B02Application {

	public static void main(String[] args) {
		SpringApplication.run(B02Application.class, args);
	}

}
