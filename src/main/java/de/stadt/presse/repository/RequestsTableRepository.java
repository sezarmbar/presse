package de.stadt.presse.repository;

import de.stadt.presse.entity.RequestsTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestsTableRepository extends JpaRepository<RequestsTable,Long> {


}
