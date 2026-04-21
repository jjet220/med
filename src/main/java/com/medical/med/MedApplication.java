package com.medical.med;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.medical,med")
public class MedApplication {
	public static void main(String[] args) {
		SpringApplication.run(MedApplication.class, args);
	}
}
