package de.stadt.presse.entity;

import com.drew.lang.annotations.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "images")
@ToString(exclude = {"keywords"})
@EqualsAndHashCode(exclude = {"keywords"})
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},allowGetters = true)
@Data
public class Image {
  public Image() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty
  private Long id;

  @NotNull
  @JsonProperty
  @Column(name = "image_name")
//  @Column(name = "image_name",unique = true)
  private String imageName;

  @NotNull
  @JsonProperty
  @Column(name = "image_path",unique = true)
  private String imagePath;

  @JsonProperty
  @Column(name = "image_thump_path")
  private String imageThumpPath;

  @JsonProperty
  @Column(name = "image_watermark_path")
  private String imageWatermarkPath;

  @JsonProperty
//  @Enumerated(value=EnumType.STRING)
  @Column(name = "image_type")
  private String imageType;

  @JsonProperty
  @Column(name = "image_all_keywords")
  @Type(type="text")
  private String imageAllKeywords;

  @JsonProperty
  @Column(name = "image_have_metadata")
  private boolean imageHaveMetadata;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  @JsonProperty
  private Date updatedAt;

  @Column(nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  @JsonProperty
  private Date createdAt;

  @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
  @JoinTable(name = "images_Keywords",
    joinColumns = { @JoinColumn(name = "image_id") },
    inverseJoinColumns = { @JoinColumn(name = "Keyword_id") })
  private Set<Keyword> keywords = new HashSet<>();

}
