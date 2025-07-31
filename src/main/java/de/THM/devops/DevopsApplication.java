package de.THM.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class DevopsApplication {

    @GetMapping("/message")
    public String message() {
        return "Congrats! you app deployed successfully in Azure!";
    }

    /**
     * The main entry point of the application. This method starts the three services
     * (customer, consultant and project) and registers a shutdown hook to close the
     * application contexts when the JVM is about to exit.
     *
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {

        SpringApplication.run(DevopsApplication.class);
    }
}
