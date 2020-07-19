package com.spiritual.junction.iyfAPI.domain;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
public class CertificateCriteria {

      //TODO
//    Integer passingPercentage;
    Integer minimumSessionAttended;
}
