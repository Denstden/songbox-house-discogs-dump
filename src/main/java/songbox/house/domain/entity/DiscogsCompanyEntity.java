package songbox.house.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@Entity
@Table
@NoArgsConstructor
public class DiscogsCompanyEntity {
    @Id
    @Column
    private Integer id;
    @Column
    private String name;
    @Column
    private String catno;
    @Column
    private Integer entityType;
    @Column
    private String entityTypeName;
    @ManyToMany(mappedBy = "companies")
    private List<DiscogsReleaseEntity> releases = new LinkedList<>();
}
