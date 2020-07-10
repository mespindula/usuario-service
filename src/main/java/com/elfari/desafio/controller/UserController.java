package com.elfari.desafio.controller;

import java.util.Calendar;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.elfari.desafio.model.User;
import com.elfari.desafio.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
@Api(value="API Rest Usuários")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Cria um novo usuário e salva na base de dados")
    public User create(@Valid @RequestBody final User user) {
		user.setCreatedAt(Calendar.getInstance());
        return userRepository.save(user);
    }
    
	@PutMapping("/{id}")
	@ApiOperation(value = "Atualiza um usuário conforme o ID informado")
	public User update(@PathVariable final Long id, @Valid @RequestBody final User user) {
		user.setId(id);
		user.setCreatedAt(Calendar.getInstance());
		return userRepository.save(user);
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Deleta um usuário conforme o ID informado")
	public ResponseEntity delete(@PathVariable final Long id) {
		User user = new User();
        user.setId(id);
		userRepository.delete(user);
		return ResponseEntity.noContent().build();
	}
     
     @GetMapping("/list")
     @ApiOperation(value = "Lista todos os usuários cadastrados na base de dados")
     public Iterable<User> list() {         
         Iterable<User> iterable = userRepository.findAll();
         return iterable;
     }
     
     @GetMapping("/{id}")
     @ApiOperation(value = "Retorna o usuário conforme ID informado")
     public Optional<User> get(@PathVariable final Long id) {       
         return userRepository.findById(id);
     }

}
