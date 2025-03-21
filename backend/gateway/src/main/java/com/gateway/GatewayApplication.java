package com.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication implements CommandLineRunner {

	@Value("${server.port}")
	private int port;

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		System.out.println("SERVER STARTED ON PORT " + port);
	}
}
