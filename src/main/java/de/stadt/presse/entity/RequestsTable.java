package de.stadt.presse.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;



@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@EqualsAndHashCode
@Entity
public class RequestsTable {
  private @Id
  @GeneratedValue
  Long id;
  private String folder;
  private String thumpPath;
  private String googleVisionLocalPath;
  private Integer scaleHeight;
  private Integer scaleHeightForGoogleVision;
  private String strText;

}
