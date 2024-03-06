package com.getechnologies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.getechnologies") 
public class TestGetechnologiesMxApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TestGetechnologiesMxApplication.class, args);
	}
}
