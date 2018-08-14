package de.stadt.presse.repository.image;


import de.stadt.presse.entity.Image;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ImageRepositoryCustomImpl implements ImageRepositoryCustom {

  @PersistenceContext
  private
  EntityManager em;

  @Override
  public List<Image> findAllKeyword(String keywordEn,Long size) {


    String query = ("select i from Image i join i.keywords k " +
      "where k.keywordEn in (" +keywordEn +") GROUP BY i.imageName HAVING count(*) = " +size);

    Query q = em.createQuery(query);

    return (List<Image>) q.getResultList();

  }

}
