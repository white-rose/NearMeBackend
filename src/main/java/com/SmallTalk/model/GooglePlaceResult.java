package com.SmallTalk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class GooglePlaceResult {

    private Geometry geometry;
    private String icon;
    private String id;
    private String name;

    @JsonProperty("photos")
    private placePhoto[] photo;

    @JsonProperty("place_id")
    private String placeId;

    private String reference;
    private String scope;
    private String[] types;
    private String vivinity;

    public GooglePlaceResult() { }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public placePhoto[] getPhoto() {
        return photo;
    }

    public void setPhoto(placePhoto[] photo) {
        this.photo = photo;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getVivinity() {
        return vivinity;
    }

    public void setVivinity(String vivinity) {
        this.vivinity = vivinity;
    }
}
