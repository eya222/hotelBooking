package com.example.demo.web.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.business.services.CustomerService;
import com.example.demo.dao.entities.Customer;
import com.example.demo.exceptions.DuplicateCustomerException;
import com.example.demo.web.dto.CustomerDTO;

import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/contacts")

public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllCustomers() {
        List<CustomerDTO> customers= this.customerService.getAllCustomers()
                .stream()
                .map(CustomerDTO::toCustomerDTO)
                // .map(contact->ContactSummaryDTO.toContactSummaryDTO(contact))
                .collect(Collectors.toList());
              
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable(name="id") Long id) {
        CustomerDTO customer = CustomerDTO.toCustomerDTO(this.customerService.getCustomerById(id));
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> addCustomer(@RequestBody CustomerDTO customerDTO) throws DuplicateCustomerException  {
       
        Customer customer = CustomerDTO.fromCustomerDTO(customerDTO);
        return new ResponseEntity<>(this.customerService.addCustomer(customer), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable(name="id") Long id, @RequestBody CustomerDTO customerDTO) throws DuplicateCustomerException {
        Customer customer = CustomerDTO.fromCustomerDTO(customerDTO);
        return new ResponseEntity<>(this.customerService.updateCustomer(id, customer), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable(name="id") Long id) {
        this.customerService.deleteCustomer(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);

    }
    @GetMapping("/{userName}/{password}")
    public ResponseEntity<Integer> checkValidUser(
        @PathVariable("userName") String userName,
        @PathVariable("password") String password
    ) {
        
        int result = customerService.checkValidUser(userName, password);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/username/{userName}")
    public ResponseEntity<CustomerDTO> getCustomerByUsername(@PathVariable("userName") String userName) {
        
        CustomerDTO customerDTO = customerService.getCustomerByUsername(userName);
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

}
 