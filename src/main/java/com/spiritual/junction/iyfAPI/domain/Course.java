package com.spiritual.junction.iyfAPI.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
public class Course extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String        title;
    String        description;
    String        thumbnail;
    String        type;
    String        isCourseLive;
    String        active;
    String        startDate;
    LocalDate     endDate;
    Integer       startTime;
    Integer       endTime;
    Integer       pricing;
    String        promoVideo;
    @ElementCollection
    List<String>  features;
    @ElementCollection
    List<String>  courseMaterial;
    @OneToMany
    List<Session> sessions;
    @Embedded
    CourseCriteria courseCriteria;
    @OneToMany
    List<CourseUserFeedback> userFeedBacks;
}
