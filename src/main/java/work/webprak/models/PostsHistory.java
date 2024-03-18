package work.webprak.models;

import lombok.*;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "PostsHistory")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class PostsHistory implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "WorkerID")
    @ToString.Exclude
    @NonNull
    private Workers worker;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PostID")
    @ToString.Exclude
    private Posts post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SubdivisionID")
    @ToString.Exclude
    private Subdivisions subdivision;

    @Column(nullable = false, name = "WorkStart")
    @NonNull
    private Timestamp work_start;

    @Column(nullable = true, name = "WorkEnd")
    private Timestamp work_end;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostsHistory other = (PostsHistory) o;
        return Objects.equals(id, other.id)
                && Objects.equals(worker, other.worker)
                && Objects.equals(post, other.post)
                && Objects.equals(subdivision, other.subdivision)
                && Objects.equals(work_start, other.work_start)
                && Objects.equals(work_end, other.work_end);
    }
}
