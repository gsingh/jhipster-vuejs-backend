package com.pmill.vuejs.repository;

import com.pmill.vuejs.domain.PictureOfEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PictureOfEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PictureOfEventRepository extends JpaRepository<PictureOfEvent, Long> {

}
