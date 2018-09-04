package de.stadt.presse.repository;

import de.stadt.presse.entity.Image;
import de.stadt.presse.repository.image.ImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
  Image findByImageName(String imageName);
  Image findByImagePath(String imagePath);
  Set<Image> findByImageHaveMetadata(boolean imageHaveMetadata);
  }
