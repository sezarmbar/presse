package de.stadt.presse.service;

import de.stadt.presse.entity.RequestsTable;
import de.stadt.presse.repository.RequestsTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestsTableService {

  @Autowired
  private RequestsTableRepository requestsTableRepository;

  public RequestsTable save(RequestsTable request){
    return requestsTableRepository.save(request);
  }


}
