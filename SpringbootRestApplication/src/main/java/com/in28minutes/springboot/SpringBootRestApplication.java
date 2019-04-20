package com.in28minutes.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("/com/in28minutes") // ComponentScan automatically searches in the same package
public class SpringBootRestApplication {

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ApplicationContext ctx = SpringApplication.run(SpringBootRestApplication.class, args);
	}

}