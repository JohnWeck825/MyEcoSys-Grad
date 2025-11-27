package com.example.myecosysgrad.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myecosysgrad.model.InvalidatedToken;

public interface InvalidatedTokenRepo extends JpaRepository<InvalidatedToken, String> {}
