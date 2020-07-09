package com.elfari.desafio.repository;

import org.springframework.data.repository.CrudRepository;

import com.elfari.desafio.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
