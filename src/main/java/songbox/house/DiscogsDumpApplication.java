package songbox.house;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

import static songbox.house.service.DiscogsDumpReader.readAll;

@SpringBootApplication
@Slf4j
public class DiscogsDumpApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DiscogsDumpApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LocalDate now = LocalDate.now();
        log.info("Starting for {}, {}", now.getYear(), now.getMonth());
        readAll(now.getYear(), now.getMonth(),
                System.out::println, System.out::println, System.out::println, System.out::println);
        log.info("Finished {}, {}", now.getYear(), now.getMonth());
    }
}
