package com.example.demo.business.services;

import java.util.List;

import com.example.demo.dao.entities.Customer;
import com.example.demo.exceptions.DuplicateCustomerException;
import com.example.demo.web.dto.CustomerDTO;

public interface CustomerService {

    
 
     public List<Customer> getAllCustomers();
    public Customer getCustomerById(Long id);
    public Customer addCustomer(Customer  customer) throws DuplicateCustomerException;
    // Update method
    public Customer updateCustomer(Long id,Customer customer) throws DuplicateCustomerException;

    public void deleteCustomer(Long id);   
    public CustomerDTO getCustomerByUsername(String userName);
    public int checkValidUser(String userName,String password);
    
    
}
