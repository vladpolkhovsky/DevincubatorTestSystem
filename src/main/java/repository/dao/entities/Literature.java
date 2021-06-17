package repository.dao.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "literature")
@Getter @Setter @RequiredArgsConstructor @ToString
public class Literature {

    @Column(name = "literatureid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    int literatureId;

    @Column(name = "description", length = 100)
    String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "questionid")
    Question question;

}