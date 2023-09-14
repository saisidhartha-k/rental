package com.cycle.rental.repository;



import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.cycle.rental.entity.User;



public interface UserRepository extends CrudRepository<User, Long>{
    public Optional<User> findByName(String name);
}