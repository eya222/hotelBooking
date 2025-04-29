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
@Table(name="bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "checkin")
    private LocalDate checkin;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "checkout")
    private LocalDate checkout;

    @Column(name = "roomNumber")
    private Long roomNumber;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false,name = "confirmed")
    private boolean confirmed;
}
