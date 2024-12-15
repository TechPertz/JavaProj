package org.example.javaproj.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaProjApplication {

    public static void main(String[] args) {
        System.out.println("Starting server with test URL http://localhost:8080/test");
        SpringApplication.run(JavaProjApplication.class, args);
    }
}
