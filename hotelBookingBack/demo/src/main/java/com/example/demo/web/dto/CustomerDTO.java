package com.example.demo.web.dto;
import com.example.demo.dao.entities.Customer;


import java.time.LocalDate;


import lombok.Builder;

@Builder
public record CustomerDTO(
    Long Id,
    String userName,
    String password,
    String name,
    LocalDate Dob,
    String email,
    String mobno,
    String identifier
    ) {
  public static CustomerDTO toCustomerDTO(Customer customer) {
    CustomerDTO customerDTO = CustomerDTO.builder()
        .Id(customer.getId())
        .userName(customer.getUserName())
        .password(customer.getPassword())
        .name(customer.getName())
        .Dob(customer.getDob())
        .email(customer.getEmail())
        .mobno(customer.getMobno())
        .identifier(customer.getIdentifier()).build();
    return customerDTO;
    
  }

  public static Customer fromCustomerDTO(CustomerDTO customerDTO) {
    Customer customer = Customer.builder()
        .Id(customerDTO.Id)
        .userName(customerDTO.userName)
        .password(customerDTO.password)
        .name(customerDTO.name)
        .Dob(customerDTO.Dob)
        .email(customerDTO.email)
        .mobno(customerDTO.mobno)
        .identifier(customerDTO.identifier).build();
    return customer;
    
  }
}
