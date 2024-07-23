package com.tnqsoftware.customer_mapping_to_user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;


@SpringBootApplication
//@OpenAPIDefinition(info = @Info(title = "Customer Mapping API", version = "2.0", description = "Customer Mapping Information"))
//@SecurityScheme(name = "javainuseapi", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class CustomerMappingToUserApplication  implements CommandLineRunner{

	
	public static void main(String[] args) {
		SpringApplication.run(CustomerMappingToUserApplication.class, args);
	}
	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:8080").allowedMethods("POST","PUT","GET");
			}
		};
	}
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
