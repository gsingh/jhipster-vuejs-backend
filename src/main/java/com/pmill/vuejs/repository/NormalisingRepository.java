package com.pmill.vuejs.repository;

import com.pmill.vuejs.domain.Normalising;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Normalising entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NormalisingRepository extends JpaRepository<Normalising, Long> {

}
