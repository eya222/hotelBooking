package com.example.demo.dao.entities;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="rooms")
public class Room {
    @Id
  private long roomNumber;
  @Column(name = "category",nullable = false)
  private String category;
  @Column(name = "roomType",nullable = false)
  private String roomType;

  @Column(name = "noOfGuests",nullable = false)
  private int noOfGuests;
  @Column(name = "price",nullable = false)
  private double price;
  @Column(nullable = true)
    private String image;
  
  
}
