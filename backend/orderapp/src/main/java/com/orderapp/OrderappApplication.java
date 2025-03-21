package com.orderapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.orderapp.service.proxy")
public class OrderappApplication implements CommandLineRunner {

	@Value("${server.port}")
	private int port;

	public static void main(String[] args) {
		SpringApplication.run(OrderappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("SERVER STARTED ON PORT " + port );
	}
}
