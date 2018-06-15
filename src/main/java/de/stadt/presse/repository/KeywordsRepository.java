package de.stadt.presse.repository;

import de.stadt.presse.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordsRepository extends JpaRepository<Keyword,Long> {

  Keyword findByKeywordEn(String keywordEn);

}
