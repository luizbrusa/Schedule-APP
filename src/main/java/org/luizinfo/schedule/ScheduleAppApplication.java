package org.luizinfo.schedule;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = "org.luizinfo.schedule.*")
@EnableTransactionManagement
public class ScheduleAppApplication {

	@PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));// It will set UTC timezone
        System.out.println("Spring boot application running in UTC timezone :" + new Date());// It will print UTC timezone
    }
	
	public static void main(String[] args) {
		SpringApplication.run(ScheduleAppApplication.class, args);
	}

}
