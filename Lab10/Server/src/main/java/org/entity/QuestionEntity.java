package org.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@NamedQueries({
    @NamedQuery(name = "Question.findAll", query = "SELECT q FROM QuestionEntity q")
})
@Getter
@Setter
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EntityListeners(AuditListener.class)
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false, length = 1000)
    private String text;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<String> options;

    @Column(name = "correct_index")
    private int correctIndex;

    @ManyToMany(mappedBy = "questions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<GameEntity> games = new ArrayList<>();

    public QuestionEntity(String text, List<String> options, int correctIndex) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    @Override
    public String toString() {
        return "QuestionEntity{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", correctIndex=" + correctIndex +
                '}';
    }
}
