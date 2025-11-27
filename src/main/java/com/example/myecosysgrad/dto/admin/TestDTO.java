package com.example.myecosysgrad.dto.admin;

import java.time.LocalDate;

public record TestDTO(String name, int age, LocalDate dob) {
    public boolean isValid() {
        return age > 0 && dob != null;
    }
}
