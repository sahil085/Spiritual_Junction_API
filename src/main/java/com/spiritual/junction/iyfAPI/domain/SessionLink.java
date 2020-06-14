package com.spiritual.junction.iyfAPI.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
public class SessionLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String platform;
    String link;
}
