package edu.springboot.organizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WebSheetApp {

    public static void main(String[] args) {
        SpringApplication.run(WebSheetApp.class, args);
    }

}
