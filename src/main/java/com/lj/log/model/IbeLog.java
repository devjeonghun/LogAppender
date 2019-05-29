package com.lj.log.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IbeLog {
    private String csid;
    private int seq;
    private String type;
    private String message;

}
