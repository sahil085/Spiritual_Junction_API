package com.spiritual.junction.iyfAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spiritual.junction.iyfAPI.domain.ParticipantSession;

@Repository
public interface ParticipantSessionRepo extends JpaRepository<ParticipantSession, Long> {


}
