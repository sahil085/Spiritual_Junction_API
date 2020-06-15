package com.spiritual.junction.iyfAPI.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String    title;
    String    description;
    String    thumbnail;
    LocalDate date;
    Integer   startTime;
    Integer   endTime;
    //    AM or PM
    String    timeType;
    @Embedded
    Speaker                   speaker;
    @OneToMany
    List<SessionLink>         links;
    @OneToMany
    List<SessionUserFeedback> userFeedBacks;
    Boolean active;
    @OneToOne
    SessionFeedback sessionFeedback;
}
