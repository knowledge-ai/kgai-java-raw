package ai.knowledge.raw;

import ai.knowledge.raw.controller.RawController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaTransformerApplication implements CommandLineRunner {

    @Autowired
    private RawController rawController;


    public static void main(String[] args) {
        SpringApplication.run(JavaTransformerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        rawController.startConsuming();
    }
}
