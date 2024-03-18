package work.webprak.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "InnerSubdivisions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class InnerSubdivisions implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MainSubdID")
    @ToString.Exclude
    @NonNull
    private Subdivisions main_subdivision;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "InnerSubdID")
    @ToString.Exclude
    @NonNull
    private Subdivisions inner_subdivision;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnerSubdivisions other = (InnerSubdivisions) o;
        return Objects.equals(id, other.id)
                && Objects.equals(main_subdivision, other.main_subdivision)
                && Objects.equals(inner_subdivision, other.inner_subdivision);
    }
}

