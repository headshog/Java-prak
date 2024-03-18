package work.webprak.models;

import lombok.*;

import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "Posts")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Posts implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ID")
    private Long id;

    @Column(nullable = false, name = "Name")
    @NonNull
    private String name;

    @Column(nullable = false, name = "Responsibilities")
    @NonNull
    private String responsibilities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posts other = (Posts) o;
        return Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(responsibilities, other.responsibilities);
    }
}
