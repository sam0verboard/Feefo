package normalisation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Entry point for the Spring Boot application. This class bootstraps the application using
 * SpringApplication and performs component scanning within the normalisation package.
 */
@SpringBootApplication
@ComponentScan(basePackages = "normalisation")
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
