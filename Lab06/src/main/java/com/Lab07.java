package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab07 {
    public static void main(String[] args) {
        SpringApplication.run(Lab07.class, args);
    }
}

// Am apelat din browser http://localhost:8081/movies si am obtinut lista
// Stiu ca in teorie conexiunea cu baza de date ar trebui mutata pe spring, dar cum nu scrie in mod explicit, am lasat asa