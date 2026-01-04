package com.lerner.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

// Unused imports – added only for Quality Gate checking
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;
import java.io.Serializable;

@SpringBootApplication
@EnableEurekaServer
public class LernerEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LernerEurekaApplication.class, args);
	}

}
