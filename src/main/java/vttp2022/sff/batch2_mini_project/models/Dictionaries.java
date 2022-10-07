package vttp2022.sff.batch2_mini_project.models;

import jakarta.json.JsonObject;

public class Dictionaries {
    private JsonObject aircraft;
    private JsonObject carriers;

    public JsonObject getAircraft() {
        return aircraft;
    }
    public void setAircraft(JsonObject aircraft) {
        this.aircraft = aircraft;
    }
    public JsonObject getCarriers() {
        return carriers;
    }
    public void setCarriers(JsonObject carriers) {
        this.carriers = carriers;
    }
    
    public Dictionaries(JsonObject aircraft, JsonObject carriers) {
        this.aircraft = aircraft;
        this.carriers = carriers;
    }
}