package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
// Am scris https//localhost:8081/movies si am gasit o pagina goala cu un json in ea
// deci am sa banuiesc ca merge
// port 8081 am ales pentru ca am ceva proces de la oracle care imi mananca portul 8080