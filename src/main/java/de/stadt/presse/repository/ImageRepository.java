package de.stadt.presse.repository;

import de.stadt.presse.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
  Image findByImageName(String imageName);
}
