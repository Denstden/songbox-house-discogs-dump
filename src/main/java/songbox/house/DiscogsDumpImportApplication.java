package songbox.house;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import songbox.house.service.dump.DiscogsDumpImportToDatabaseService;
import songbox.house.service.dump.DiscogsDumpIterateService;

import java.time.LocalDate;

import static java.time.LocalDate.of;
import static java.time.Month.MAY;
import static lombok.AccessLevel.PRIVATE;

@SpringBootApplication
@Slf4j
@Component
@FieldDefaults(makeFinal = true, level = PRIVATE)
/*
total added releases 12 452 000 (12452000)
0.5 sec 10 -> ~ 1min 1200 -> 1h 72000 -> 172h needed for full releases import
*/
public class DiscogsDumpImportApplication implements CommandLineRunner {

    DiscogsDumpImportToDatabaseService discogsDumpImportToDatabaseService;
    DiscogsDumpIterateService discogsDumpIterateService;

    @Autowired
    public DiscogsDumpImportApplication(DiscogsDumpImportToDatabaseService discogsDumpImportToDatabaseService,
            DiscogsDumpIterateService discogsDumpIterateService) {
        this.discogsDumpImportToDatabaseService = discogsDumpImportToDatabaseService;
        this.discogsDumpIterateService = discogsDumpIterateService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DiscogsDumpImportApplication.class, args);
    }

    @Override
    public void run(String... args) {
        LocalDate date = of(2020, MAY, 1);

//        discogsDumpIterateService.processDump(date);

        discogsDumpImportToDatabaseService.processDump(date);
    }
}
