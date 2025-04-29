package com.example.demo.dao.entities;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;


import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="customers")
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;
  @Column(name = "userName",unique = true,nullable = false)
  private String userName;

  @Column(name = "password" ,nullable = false)
  private String password;

  @Column(name = "name")
  private String name;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Column(name = "Dob",nullable = false)
  private LocalDate Dob;

  @Column(name = "email",nullable = false,unique = true)
  private String email;
  @Column(name = "mobno",nullable = false)
  private String mobno;



  @Column(name = "identifier",nullable = false)
  private String identifier;
}
