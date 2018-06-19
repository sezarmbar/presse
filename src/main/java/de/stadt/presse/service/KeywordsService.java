package de.stadt.presse.service;

import de.stadt.presse.entity.Keyword;
import de.stadt.presse.repository.KeywordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.util.List;


@Service
public class KeywordsService {
  @Autowired
  KeywordsRepository keywordsRepository;

  public Keyword findByKeywordEn(String keywordEn) {
      return keywordsRepository.findByKeywordEn(keywordEn);
  }


}
