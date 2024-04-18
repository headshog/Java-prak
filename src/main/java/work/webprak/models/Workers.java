package work.webprak.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Workers")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Workers implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ID")
    private Long id;

    @Column(nullable = false, name = "Name")
    @NonNull
    private String name;

    @Column(nullable = false, name = "Address")
    @NonNull
    private String address;

    @Column(name = "Graduation")
    @NonNull
    private String graduation;

    @Column(name = "Experience")
    @NonNull
    private Long experience;

    @Column(nullable = false, name = "BirthDate")
    @NonNull
    private String birthDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workers other = (Workers) o;
        return Objects.equals(id, other.id)
                && name.equals(other.name)
                && address.equals(other.address)
                && Objects.equals(graduation, other.graduation)
                && Objects.equals(experience, other.experience);
    }
}
