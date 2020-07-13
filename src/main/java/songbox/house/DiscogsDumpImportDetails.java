package songbox.house;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static java.lang.String.valueOf;
import static java.text.MessageFormat.format;
import static java.time.LocalDate.of;
import static java.time.format.DateTimeFormatter.ofPattern;

public class DiscogsDumpImportDetails {
    private static final DateTimeFormatter FORMATTER = ofPattern("yyyyMMdd");

    private final int day;
    private final int year;
    private final Month month;
    private final DiscogsDumpImportType type;

    public DiscogsDumpImportDetails(int day, int year, Month month, DiscogsDumpImportType type) {
        this.day = day;
        this.year = year;
        this.month = month;
        this.type = type;
    }

    public DiscogsDumpImportDetails(LocalDate date, DiscogsDumpImportType type) {
        this.day = date.getDayOfMonth();
        this.year = date.getYear();
        this.month = date.getMonth();
        this.type = type;
    }

    public DiscogsDumpImportDetails(DiscogsDumpImportType type) {
        LocalDate now = LocalDate.now();
        this.day = now.getDayOfMonth();
        this.year = now.getYear();
        this.month = now.getMonth();
        this.type = type;
    }

    public String getUrl() {
        return format("https://discogs-data.s3-us-west-2.amazonaws.com/data/{0}/discogs_{1}_{2}.xml.gz",
                valueOf(year),
                FORMATTER.format(of(year, month.getValue(), day)),
                type.getType()
        );
    }

    public DiscogsDumpImportType getType() {
        return type;
    }
}
