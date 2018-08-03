package de.stadt.presse.repository.image;

import de.stadt.presse.entity.Image;

import java.util.List;

  public interface ImageRepositoryCustom {
  public List<Image> findAllKeyword(List<String> keywordEn);

}
