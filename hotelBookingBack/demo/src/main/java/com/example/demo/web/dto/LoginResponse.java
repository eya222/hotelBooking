package com.example.demo.web.dto;

public record LoginResponse(
    Long id,
    String username,
    String name,
    String email
) {} 