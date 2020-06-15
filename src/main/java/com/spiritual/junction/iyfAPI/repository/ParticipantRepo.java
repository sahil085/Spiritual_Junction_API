package com.spiritual.junction.iyfAPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spiritual.junction.iyfAPI.domain.Participant;


@Repository
public interface ParticipantRepo extends JpaRepository<Participant, Long> {

    Participant findByEmail(String email);

    @Query(value = "select p.email from Participant p inner join ParticipantSession ps on ps.participant_id = p.id group by p.email having count(p.email) >= " +
            "4", nativeQuery = true)
    List<String> findAllByParticipationGreaterThanEqualToFour();

    List<Participant> findAllByEmailIn(List<String> emails);
}
