package songbox.house.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.LinkedList;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Setter
@Getter
@Entity
@Table
@NoArgsConstructor
public class DiscogsReleaseVideoEntity {
    @Id
    @Column
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Column
    private String src;
    @Column
    private String duration;
    @Column
    private Boolean embed;
    @Column
    private String title;
    @Column
    private String description;
    @ManyToMany(mappedBy = "videos")
    private List<DiscogsReleaseEntity> releases = new LinkedList<>();
}
