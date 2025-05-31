package com.perfulandia.ventas_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages ={"com.perfulandia.ventas_api"})
public class VentasApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VentasApiApplication.class, args);
	}

}
