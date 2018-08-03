package de.stadt.presse.repository;

import de.stadt.presse.entity.Image;
import de.stadt.presse.repository.image.ImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
  Image findByImageName(String imageName);
  Image findByImagePath(String imagePath);

  @Query("select i.id, i.imageThumpPath, i.imageWatermarkPath from Image i join i.keywords k where k.keywordEn in :keywordEn")
  List<Image> findAllKeywords(@Param("keywordEn") List<String> keywordEn) ;


  }
