package de.THM.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ConsultantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultantServiceApplication.class, args);
    }

}
