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
public class DiscogsReleaseFormatEntity {
    @Id
    @Column
    @GeneratedValue(strategy = AUTO)
    private Integer id;
    @Column
    private String name;
    @Column
    private String qty;
    @Column
    private String text;
    @ManyToMany(mappedBy = "formats")
    private List<DiscogsReleaseEntity> releases = new LinkedList<>();

    //	 Missing array of descriptions

    //	<descriptions>
    //	    <description>12"</description>
    //	    <description>33 ⅓ RPM</description>
    //	</descriptions>
}
