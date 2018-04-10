package com.SmallTalk.model;

import com.SmallTalk.model.Location.GooglePlaceResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class DetailedResponse {

    @JsonProperty
    private String[] html_attributions;

    @JsonProperty
    private String next_page_token;

    @JsonProperty("results")
    private GooglePlaceResult[] places;

    public GooglePlaceResult[] getPlaces() {
        return places;
    }

    public void setPlaces(GooglePlaceResult[] places) {
        this.places = places;
    }
}
