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
  EntityManager em;

  @Override
  public List<Image> findAllKeyword(List<String> keywordEn) {


    StringBuffer query = new StringBuffer();
    query.append("select i.id, i.imageThumpPath, i.imageWatermarkPath from Image i join i.keywords k where ");

    List<String> criteria = new ArrayList<>();
    for (String key : keywordEn) {
      criteria.add("k.keywordEn = '"+ key+"'");
    }

    for (int i = 0; i < criteria.size(); i++) {
      if (i > 0) {
        query.append(" and ");
      }
      query.append(criteria.get(i));
    }

    Query q = em.createQuery(query.toString());

    return (List<Image>) q.getResultList();


  }


}
