package pressBiro.entity;

import javax.persistence.*;

@Entity
public class ImageIndex {

  private Long imageId;

  private String imageName;
  private String added_date;
  private String imageKeywords;
  private String imageType;
  private String imagePath;
  private String imageThumpPath;

}
