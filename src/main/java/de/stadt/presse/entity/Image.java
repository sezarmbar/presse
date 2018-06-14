package de.stadt.presse.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "image_index")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
  allowGetters = true)
@Data
public class Image {
  public Image() {
  }

  @Id

  @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonProperty
  private Long imageId;

  @NotBlank
  @JsonProperty
  @Column(name = "image_name")
  private String imageName;

  @NotBlank
  @JsonProperty
  @Column(name = "image_path")
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
  @Column(name = "image_keywords")
  @Type(type="text")
  private String imageKeywords;

  @JsonProperty
  @Column(name = "image_have_metadata")
  private boolean imageHaveMetadata;

  @Column(nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  @JsonProperty
  private Date createdAt;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  @JsonProperty
  private Date updatedAt;


}
