package com.spiritual.junction.iyfAPI.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class SessionFeedback extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToMany
    List<SessionFeedbackQuestion> feedbackQuestions;
    @OneToOne
    Session                       session;
}
