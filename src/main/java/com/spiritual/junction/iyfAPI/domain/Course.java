package com.spiritual.junction.iyfAPI.domain;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Course extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String    title;
    String    description;
    String    thumbnail;
    String    type;
    Boolean   active;
    LocalDate startDate;
    LocalDate endDate;
    Integer   pricing;
    String    promoVideo;
    @ElementCollection
    List<String>             features;
    @ElementCollection
    List<String>             courseMaterial;
    @OneToMany(cascade = CascadeType.PERSIST)
    List<Session>            sessions;
    @Embedded
    CourseCriteria           courseCriteria;
    @OneToMany
    List<CourseUserFeedback> userFeedBacks;
}
