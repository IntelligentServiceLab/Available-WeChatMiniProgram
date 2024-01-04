package com.sucker.infoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sucker"})
@EnableScheduling
public class InfoApplication {

  public static void main(String[] args) {
    SpringApplication.run(InfoApplication.class, args);
  }
}
