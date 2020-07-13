package songbox.house.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

@Setter
@Getter
@Entity
@Table
@NoArgsConstructor
@ToString
public class DiscogsTrackEntity {
    @Id
    @Column
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Column
    private String title;
    @Column
    private String duration;
    @Column
    private String position;
    @ManyToMany(mappedBy = "tracks")
    private List<DiscogsReleaseEntity> releases = new LinkedList<>();
    @ManyToMany(cascade = ALL)
    private List<DiscogsTrackArtistEntity> artists = new LinkedList<>();
    @ManyToMany(cascade = ALL)
    private List<DiscogsTrackExtraArtistEntity> extraArtists = new LinkedList<>();
}
