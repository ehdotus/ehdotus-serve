package ehdotus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class ServeApp implements CommandLineRunner {

    @Autowired
    private DataServer dataServer;

    public static void main(String[] args) {
        SpringApplication.run(ServeApp.class, args);
    }

    @Override
    public void run(String... args) {
        dataServer.run();
    }
}
