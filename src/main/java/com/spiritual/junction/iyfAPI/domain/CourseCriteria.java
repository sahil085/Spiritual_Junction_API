package com.spiritual.junction.iyfAPI.domain;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
public class CourseCriteria {


    Integer passingPercentage;
    Integer minimumSessionAttended;
}
