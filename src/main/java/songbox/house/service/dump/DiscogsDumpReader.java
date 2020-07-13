package songbox.house.service.dump;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import songbox.house.DiscogsDumpImportDetails;
import tslic.discogs.dump.DumpRecords;
import tslic.discogs.dump.models.Artist;
import tslic.discogs.dump.models.Label;
import tslic.discogs.dump.models.Master;
import tslic.discogs.dump.models.Release;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

import static songbox.house.DiscogsDumpImportType.ARTISTS;
import static songbox.house.DiscogsDumpImportType.LABELS;
import static songbox.house.DiscogsDumpImportType.MASTERS;
import static songbox.house.DiscogsDumpImportType.RELEASES;

@Slf4j
@Service
public class DiscogsDumpReader {

    public static boolean readAll(int year, Month month, Consumer<Release> releaseConsumer,
            Consumer<Artist> artistConsumer, Consumer<Master> masterConsumer, Consumer<Label> labelConsumer) {

        return readReleases(1, year, month, releaseConsumer) && readArtists(year, month, artistConsumer) &&
                readMasters(year, month, masterConsumer) && readLabels(year, month, labelConsumer);
    }

    public static boolean readReleases(int day, int year, Month month, Consumer<Release> consumer) {
        DiscogsDumpImportDetails importDetails = new DiscogsDumpImportDetails(day, year, month, RELEASES);
        return readData(importDetails.getUrl(), DumpRecords::readReleases, consumer);
    }

    public static boolean readReleases(LocalDate date, Consumer<Release> consumer) {
        return readReleases(date.getDayOfMonth(), date.getYear(), date.getMonth(), consumer);
    }

    public static boolean readReleasesNow(Consumer<Release> consumer) {
        LocalDate now = LocalDate.now();
        DiscogsDumpImportDetails importDetails = new DiscogsDumpImportDetails(now, RELEASES);
        return readData(importDetails.getUrl(), DumpRecords::readReleases, consumer);
    }

    public static boolean readArtists(int year, Month month, Consumer<Artist> consumer) {
        DiscogsDumpImportDetails importDetails = new DiscogsDumpImportDetails(1, year, month, ARTISTS);
        return readData(importDetails.getUrl(), DumpRecords::readArtists, consumer);
    }

    public static boolean readMasters(int year, Month month, Consumer<Master> consumer) {
        DiscogsDumpImportDetails importDetails = new DiscogsDumpImportDetails(1, year, month, MASTERS);
        return readData(importDetails.getUrl(), DumpRecords::readMasters, consumer);
    }

    public static boolean readLabels(int year, Month month, Consumer<Label> consumer) {
        DiscogsDumpImportDetails importDetails = new DiscogsDumpImportDetails(1, year, month, LABELS);
        return readData(importDetails.getUrl(), DumpRecords::readLabels, consumer);
    }

    private static <T> boolean readData(String url, Function<InputStream, Iterator<T>> readFunction,
            Consumer<T> consumer) {

        try (InputStream inputStream = new URL(url).openStream()) {
            try (InputStream is = new GZIPInputStream(inputStream)) {
                Iterator<T> items = readFunction.apply(is);
                while (items.hasNext()) {
                    consumer.accept(items.next());
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Can't read {}", url, e);
            return false;
        }
    }

}
