package com.perfulandia.productos_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages ={"com.perfulandia.productos_api"})
public class ProductosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosApiApplication.class, args);
	}

}
