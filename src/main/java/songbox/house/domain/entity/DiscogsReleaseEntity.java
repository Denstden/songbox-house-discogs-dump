package songbox.house.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Setter
@Getter
@Entity
@Table
@NoArgsConstructor
@ToString
public class DiscogsReleaseEntity {

    @Id
    @Column
    private Integer id;
    @Column
    private String title;
    @Column
    private String country;
    @Column(length = 65535)
    private String notes;
    @Column
    private String released;
    @Column
    private String status;
    @Column
    private String dataQuality;
    @Column
    private Integer masterId;

    @ManyToMany(cascade = ALL)
    private List<DiscogsReleaseArtistEntity> artists = new LinkedList<>();
    @ManyToMany(cascade = ALL)
    private List<DiscogsReleaseExtraArtistEntity> extraArtists = new LinkedList<>();
    @ManyToMany(cascade = ALL)
    private List<DiscogsReleaseFormatEntity> formats = new LinkedList<>();
    @ManyToMany(cascade = ALL)
    private List<DiscogsReleaseVideoEntity> videos = new LinkedList<>();
    @ManyToMany(cascade = ALL)
    private List<DiscogsCompanyEntity> companies = new LinkedList<>();
    @ManyToMany(cascade = ALL)
    private List<DiscogsReleaseLabelEntity> labels = new LinkedList<>();
    @ManyToMany(cascade = ALL)
    private List<DiscogsTrackEntity> tracks = new LinkedList<>();


    //    private List<String> genres = new LinkedList<>();
//
//    private List<String> styles = new LinkedList<>();
}
