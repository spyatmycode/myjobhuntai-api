package com.spyatmycode.myjobhuntai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJdbcAuditing
@EnableAsync //
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
