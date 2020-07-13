package songbox.house.converter;

import songbox.house.domain.entity.DiscogsCompanyEntity;
import songbox.house.domain.entity.DiscogsReleaseArtistEntity;
import songbox.house.domain.entity.DiscogsReleaseEntity;
import songbox.house.domain.entity.DiscogsReleaseExtraArtistEntity;
import songbox.house.domain.entity.DiscogsReleaseFormatEntity;
import songbox.house.domain.entity.DiscogsReleaseLabelEntity;
import songbox.house.domain.entity.DiscogsReleaseVideoEntity;
import songbox.house.domain.entity.DiscogsTrackArtistEntity;
import songbox.house.domain.entity.DiscogsTrackEntity;
import songbox.house.domain.entity.DiscogsTrackExtraArtistEntity;
import tslic.discogs.dump.models.Company;
import tslic.discogs.dump.models.Release;
import tslic.discogs.dump.models.ReleaseArtist;
import tslic.discogs.dump.models.ReleaseExtraArtist;
import tslic.discogs.dump.models.ReleaseFormat;
import tslic.discogs.dump.models.ReleaseLabel;
import tslic.discogs.dump.models.ReleaseVideo;
import tslic.discogs.dump.models.Track;
import tslic.discogs.dump.models.TrackArtist;
import tslic.discogs.dump.models.TrackExtraArtist;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class DiscogsReleaseConverter implements Converter<Release, DiscogsReleaseEntity> {
    @Override
    public DiscogsReleaseEntity convert(Release release) {
        DiscogsReleaseEntity entity = new DiscogsReleaseEntity();
        entity.setId(release.getId());
        entity.setCountry(release.getCountry());
        entity.setDataQuality(release.getDataQuality());
        entity.setMasterId(release.getMasterId());
        entity.setNotes(release.getNotes());
        entity.setReleased(release.getReleased());
        entity.setTitle(release.getTitle());
        entity.setStatus(release.getStatus());
        //TODO
//        entity.setGenres(release.getGenres());
//        entity.setStyles(release.getStyles());
        entity.setArtists(convert(release.getArtists(), this::convert));
        entity.setExtraArtists(convert(release.getExtraArtists(), this::convert));
        entity.setCompanies(convert(release.getCompanies(), this::convert));
        entity.setFormats(convert(release.getFormats(), this::convert));
        entity.setLabels(convert(release.getLabels(), this::convert));
        entity.setTracks(convert(release.getTracks(), this::convert));
        entity.setVideos(convert(release.getVideos(), this::convert));
        return entity;
    }

    private <E, D> List<E> convert(List<D> dtos, Function<D, E> function) {
        return dtos.stream()
                .map(function)
                .collect(toList());
    }

    private <E, D> Set<E> convertToSet(List<D> dtos, Function<D, E> function) {
        return dtos.stream()
                .map(function)
                .collect(toSet());
    }

    private DiscogsReleaseExtraArtistEntity convert(ReleaseExtraArtist artist) {
        DiscogsReleaseExtraArtistEntity entity = new DiscogsReleaseExtraArtistEntity();
        entity.setId(artist.getId());
        entity.setAnv(artist.getAnv());
        entity.setJoin(artist.getJoin());
        entity.setTracks(artist.getTracks());
        entity.setName(artist.getName());
        entity.setRole(artist.getRole());
        return entity;
    }

    private DiscogsReleaseArtistEntity convert(ReleaseArtist artist) {
        DiscogsReleaseArtistEntity entity = new DiscogsReleaseArtistEntity();
        entity.setId(artist.getId());
        entity.setAnv(artist.getAnv());
        entity.setJoin(artist.getJoin());
        entity.setTracks(artist.getTracks());
        entity.setName(artist.getName());
        return entity;
    }

    private DiscogsCompanyEntity convert(Company company) {
        DiscogsCompanyEntity entity = new DiscogsCompanyEntity();
        entity.setId(company.getId());
        entity.setCatno(company.getCatno());
        entity.setEntityType(company.getEntityType());
        entity.setEntityTypeName(company.getEntityTypeName());
        entity.setName(company.getName());
        return entity;
    }

    private DiscogsReleaseFormatEntity convert(ReleaseFormat format) {
        DiscogsReleaseFormatEntity entity = new DiscogsReleaseFormatEntity();
        entity.setName(format.getName());
        entity.setQty(format.getQty());
        entity.setText(format.getText());
        return entity;
    }

    private DiscogsReleaseLabelEntity convert(ReleaseLabel label) {
        DiscogsReleaseLabelEntity entity = new DiscogsReleaseLabelEntity();
        entity.setName(label.getName());
        entity.setCatno(label.getCatno());
        entity.setId(label.getId());
        return entity;
    }

    private DiscogsTrackEntity convert(Track track) {
        DiscogsTrackEntity entity = new DiscogsTrackEntity();
        entity.setDuration(track.getDuration());
        entity.setPosition(track.getPosition());
        entity.setTitle(track.getTitle());
        entity.setArtists(convert(track.getArtists(), this::convert));
        entity.setExtraArtists(convert(track.getExtraArtists(), this::convert));
        return entity;
    }

    private DiscogsTrackArtistEntity convert(TrackArtist track) {
        DiscogsTrackArtistEntity entity = new DiscogsTrackArtistEntity();
        entity.setAnv(track.getAnv());
        entity.setId(track.getId());
        entity.setJoin(track.getJoin());
        entity.setName(track.getName());
        return entity;
    }

    private DiscogsTrackExtraArtistEntity convert(TrackExtraArtist track) {
        DiscogsTrackExtraArtistEntity entity = new DiscogsTrackExtraArtistEntity();
        entity.setAnv(track.getAnv());
        entity.setId(track.getId());
        entity.setJoin(track.getJoin());
        entity.setName(track.getName());
        entity.setRole(track.getRole());
        return entity;
    }

    private DiscogsReleaseVideoEntity convert(ReleaseVideo video) {
        DiscogsReleaseVideoEntity entity = new DiscogsReleaseVideoEntity();
        entity.setDescription(video.getDescription());
        entity.setDuration(video.getDuration());
        entity.setEmbed(video.getEmbed());
        entity.setSrc(video.getSrc());
        entity.setTitle(video.getTitle());
        return entity;
    }
}
