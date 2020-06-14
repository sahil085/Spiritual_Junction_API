package com.spiritual.junction.iyfAPI.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.spiritual.junction.iyfAPI.SessionFeedbackQuestionType;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class SessionFeedbackQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String question;

    @Enumerated(EnumType.STRING)
    SessionFeedbackQuestionType questionType;


}
