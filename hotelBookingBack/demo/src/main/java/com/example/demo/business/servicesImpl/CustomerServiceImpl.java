package com.example.demo.business.servicesImpl;

import java.util.List;
import java.util.Optional;


import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.example.demo.business.services.CustomerService;
import com.example.demo.business.services.FilesStorageService;
import com.example.demo.dao.entities.Customer;
import com.example.demo.dao.repositories.CustomerRepository;
import com.example.demo.exceptions.DuplicateCustomerException;
import com.example.demo.web.dto.CustomerDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

  
    private final CustomerRepository customerRepository;
    

    public CustomerServiceImpl(CustomerRepository customerRepository,
                              FilesStorageService filesStorageService) {
        this.customerRepository = customerRepository;
    }


    // Sort contacts by name in ascending alphabetical order
    @Override
    public List<Customer> getAllCustomers() {
        return this.customerRepository.findAll(Sort.by(Direction.ASC, "name"));
    }

    @Override
    public Customer getCustomerById(Long id) {
        // Check if the ID is null and throw an IllegalArgumentException if it is
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        // Retrieve the contact by ID, throw an EntityNotFoundException if not found
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact with id: " + id + " not found"));
    }

    // Add method
    @Override
    public Customer addCustomer(Customer customer) throws DuplicateCustomerException {
        // Check if the contact is null and throw an IllegalArgumentException if it is
        if (customer == null) {
            throw new IllegalArgumentException("customer cannot be null");
        }
        try {
            // Save the contact in the repository
            return customerRepository.save(customer);
        } catch (DataIntegrityViolationException e) {
            // Handle uniqueness constraint violations
            throw new DuplicateCustomerException(
                    "A contact with the same email or other unique field already exists.");
        }
    }
   // Update method
   @Override
   public Customer updateCustomer(Long id, Customer customer) throws DuplicateCustomerException {
       // Check if the ID or customer is null and throw an IllegalArgumentException if they are
       if (id == null || customer == null) {
           throw new IllegalArgumentException("ID or customer cannot be null");
       }
   
       // Fetch the existing customer
       Customer existingCustomer = getCustomerById(id);
   
       // Check if the email has changed
       if (!existingCustomer.getEmail().equals(customer.getEmail())) {
           if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
               throw new DuplicateCustomerException("A customer with the same email already exists.");
           }
       }
   
       // Check if the identifier has changed
       if (!existingCustomer.getIdentifier().equals(customer.getIdentifier())) {
           if (customerRepository.findByIdentifier(customer.getIdentifier()).isPresent()) {
               throw new DuplicateCustomerException("A customer with the same identifier already exists.");
           }
       }
   
       // Now update the fields manually (to avoid detaching issues)
       existingCustomer.setUserName(customer.getUserName());
       existingCustomer.setPassword(customer.getPassword());
       existingCustomer.setName(customer.getName());
       existingCustomer.setDob(customer.getDob());
       existingCustomer.setEmail(customer.getEmail());
       existingCustomer.setMobno(customer.getMobno());
       existingCustomer.setIdentifier(customer.getIdentifier());
   
       try {
           return customerRepository.save(existingCustomer);
       } catch (DataIntegrityViolationException e) {
           throw new DuplicateCustomerException(
                   "A customer with the same email or other unique field already exists.");
       }
   }
   

    @Override
    @Transactional
    // the deleteContact method executes all its operations (checking for the contact, deleting the file, 
    //and deleting the contact record) within a single transaction.If any part of this process fails, 
    //the entire transaction will be rolled back, maintaining data consistency and integrity. 
    public void deleteCustomer(Long id) {
        // Check if the ID is null and throw an IllegalArgumentException if it is
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try {
            customerRepository.deleteById(id);
        } catch (DataAccessException e) {
            // Capture any data access exceptions (e.g., foreign key constraint violations)
            throw new RuntimeException("Failed to delete customer with id: " + id, e);
        }
    }
    //public Customer findByuserName(String userName){
    //    return customerRepository.findByUserName(userName);

    //}
    public int checkValidUser(String userName, String password) {
        Optional<Customer> customerOpt = customerRepository.findByUserName(userName);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            // Compare plain-text passwords
            if (customer.getPassword().equals(password)) {
                String identifier = customer.getIdentifier().toLowerCase();
                // Return 1 if identifier starts with "admin", 2 if "user"
                if (identifier.startsWith("admin")) {
                    return 1;
                } else if (identifier.startsWith("user")) {
                    return 2;
                }
            }
        }
        return 0; // Invalid user, password, or identifier
    }
    public CustomerDTO getCustomerByUsername(String userName) {
        Optional<Customer> customerOpt = customerRepository.findByUserName(userName);
        if (customerOpt.isEmpty()) {
            
        }
        return CustomerDTO.toCustomerDTO(customerOpt.get());
    }
}

    


