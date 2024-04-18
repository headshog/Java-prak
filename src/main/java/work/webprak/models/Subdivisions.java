package work.webprak.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Subdivisions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Subdivisions implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HeadSubdID")
    @ToString.Exclude
    private Subdivisions headSubdivision;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DirectorID")
    @ToString.Exclude
    private Workers director;

    @Column(nullable = false, name = "Name")
    @NonNull
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subdivisions other = (Subdivisions) o;
        return Objects.equals(id, other.id)
                && Objects.equals(headSubdivision, other.headSubdivision)
                && Objects.equals(director, other.director)
                && Objects.equals(name, other.name);
    }
}
