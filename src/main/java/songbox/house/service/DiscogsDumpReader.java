package songbox.house.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tslic.discogs.dump.DumpRecords;
import tslic.discogs.dump.models.Artist;
import tslic.discogs.dump.models.Label;
import tslic.discogs.dump.models.Master;
import tslic.discogs.dump.models.Release;

import java.io.InputStream;
import java.net.URL;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

import static java.lang.String.valueOf;
import static java.text.MessageFormat.format;
import static java.time.LocalDate.of;
import static java.time.format.DateTimeFormatter.ofPattern;

@Slf4j
@Service
public class DiscogsDumpReader {

    private static final DateTimeFormatter FORMATTER = ofPattern("yyyyMMdd");

    public static boolean readAll(int year, Month month, Consumer<Release> releaseConsumer,
            Consumer<Artist> artistConsumer, Consumer<Master> masterConsumer, Consumer<Label> labelConsumer) {

        return readReleases(year, month, releaseConsumer) && readArtists(year, month, artistConsumer) &&
                readMasters(year, month, masterConsumer) && readLabels(year, month, labelConsumer);
    }

    public static boolean readReleases(int year, Month month, Consumer<Release> consumer) {
        String url = getUrl(year, month, "releases");
        return readData(url, DumpRecords::readReleases, consumer);
    }

    public static boolean readArtists(int year, Month month, Consumer<Artist> consumer) {
        String url = getUrl(year, month, "artists");
        return readData(url, DumpRecords::readArtists, consumer);
    }

    public static boolean readMasters(int year, Month month, Consumer<Master> consumer) {
        String url = getUrl(year, month, "masters");
        return readData(url, DumpRecords::readMasters, consumer);
    }

    public static boolean readLabels(int year, Month month, Consumer<Label> consumer) {
        String url = getUrl(year, month, "labels");
        return readData(url, DumpRecords::readLabels, consumer);
    }

    private static String getUrl(int year, Month month, String type) {
        return format("https://discogs-data.s3-us-west-2.amazonaws.com/data/{0}/discogs_{1}_{2}.xml.gz",
                valueOf(year),
                FORMATTER.format(of(year, month.getValue(), 1)),
                type
        );
    }

    private static <T> boolean readData(String url, Function<InputStream, Iterator<T>> readFunction,
            Consumer<T> consumer) {

        try (InputStream inputStream = new URL(url).openStream()) {
            try (InputStream is = new GZIPInputStream(inputStream)) {
                Iterator<T> items = readFunction.apply(is);
                while (items.hasNext()) {
                    T item = items.next();
                    consumer.accept(item);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Can't read {}", url, e);
            return false;
        }
    }

}
