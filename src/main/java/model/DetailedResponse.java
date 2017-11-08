package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class DetailedResponse {

    @JsonProperty("results")
    private GooglePlaceResult[] places;

    public GooglePlaceResult[] getPlaces() {
        return places;
    }

    public void setPlaces(GooglePlaceResult[] places) {
        this.places = places;
    }
}
