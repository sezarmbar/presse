package de.stadt.presse.entity;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;





@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt"},allowGetters = true)
@Data @NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString @EqualsAndHashCode
public class RequestsTable {
  private @Id
  @GeneratedValue
  @JsonProperty
  Long id;
  @JsonProperty
  private String folder;
  @JsonProperty
  private String thumpPath;
  @JsonProperty
  private String googleVisionLocalPath;
  @JsonProperty
  private Integer scaleHeight;
  @JsonProperty
  private Integer scaleHeightForGoogleVision;
  @JsonProperty
  private String strText;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  @JsonProperty
  private Date createdAt;

  @JsonProperty
  private String status;

}
