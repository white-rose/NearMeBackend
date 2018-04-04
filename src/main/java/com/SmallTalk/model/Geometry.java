package com.SmallTalk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class Geometry {

    private Location placeLocation;

}
