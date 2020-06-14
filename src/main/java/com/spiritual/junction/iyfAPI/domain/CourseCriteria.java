package com.spiritual.junction.iyfAPI.domain;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Embeddable
public class CourseCriteria {

    Long passingPercentage;
    Long minimumSessionAttended;
}
