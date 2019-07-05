package com.pmill.vuejs.repository;

import com.pmill.vuejs.domain.ShiftManager;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShiftManager entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShiftManagerRepository extends JpaRepository<ShiftManager, Long> {

}
