package com.elfari.desafio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.elfari.desafio.security.AccountCredentials;

import io.swagger.annotations.ApiOperation;

@SpringBootApplication
@RestController
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	
	@PostMapping("/sign-up")
	@ApiOperation(value = "Recupera o token (header da resposta) através das credenciais enviadas")
 	public void signUp(@RequestBody AccountCredentials credentials) { 	}

}
