package com.example.myecosysgrad;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class test {
    public static void main(String[] args) {
        String password = "admin";
        String hash = "$10$.ri4tFmyMaJ48RwbY42/CuodTolUrFxswuvvgwUJKtZyMFqaPvd7u";
        System.out.println(new BCryptPasswordEncoder().encode(password));
        //        System.out.println(new );
    }
}
