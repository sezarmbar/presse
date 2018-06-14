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
  @Column(name = "keyword_name")
  private String keywordName;

  @NotBlank
  @JsonProperty
  @Column(name = "keyword_name_De")
  private String keywordNameDe;


@ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            },
            mappedBy = "keywords")
    private Set<Image> images= new HashSet<>();
}
