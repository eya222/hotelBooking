package com.example.demo.dao.repositories;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.dao.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserName(String userName);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByIdentifier(String identifier);
        
}
