package com.spiritual.junction.iyfAPI.domain;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;

@Data
@Embeddable
@Builder
public class Speaker {

    String name;
    String designation;
    String speakerImage;
}
