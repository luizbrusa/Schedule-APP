package org.luizinfo.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = "org.luizinfo.schedule.*")
@EnableTransactionManagement
@EnableAutoConfiguration
public class ScheduleAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduleAppApplication.class, args);
	}

}
