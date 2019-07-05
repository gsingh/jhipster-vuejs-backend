package com.pmill.vuejs.repository;

import com.pmill.vuejs.domain.VideoOfEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the VideoOfEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoOfEventRepository extends JpaRepository<VideoOfEvent, Long> {

}
