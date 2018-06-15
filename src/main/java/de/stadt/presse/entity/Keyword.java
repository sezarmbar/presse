package de.stadt.presse.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "keywords")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Keyword {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonProperty
  private Long id;

  @NotBlank
  @JsonProperty
  @Column(name = "keyword_en")
  private String keywordEn;

//  @NotBlank
  @JsonProperty
  @Column(name = "keyword_De")
  private String keywordDe;


@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            },
            mappedBy = "keywords")
    private Set<Image> images= new HashSet<>();

  public Keyword(String keywordEn) {
    this.keywordEn = keywordEn;
  }

  @Override
  public String toString() {
    return "Keyword{" +
      "keywordEn='" + keywordEn + '\'' +
      '}';
  }
}
