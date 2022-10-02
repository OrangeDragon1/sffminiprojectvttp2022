package vttp2022.sff.batch2_mini_project.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Segment {

    private String id;
    private String departureAirport;
    private String departureDT;
    private String arrivalAirport;
    private String arrivalDT;
    private String flightNumber;
    private String operatorCode;
    private String aircraftCode;
    private String duration;
    private String cabin;
    private String includedCheckedBags;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDepartureAirport() { return departureAirport; }
    public void setDepartureAirport(String departureAirport) { this.departureAirport = departureAirport; }
    public String getDepartureDT() { return departureDT; }
    public void setDepartureDT(String departureDT) { this.departureDT = departureDT; }
    public String getArrivalAirport() { return arrivalAirport; }
    public void setArrivalAirport(String arrivalAirport) { this.arrivalAirport = arrivalAirport; }
    public String getArrivalDT() { return arrivalDT; }
    public void setArrivalDT(String arrivalDT) { this.arrivalDT = arrivalDT; }
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public String getOperatorCode() { return operatorCode; }
    public void setOperatorCode(String operatorCode) { this.operatorCode = operatorCode; }
    public String getAircraftCode() { return aircraftCode; }
    public void setAircraftCode(String aircraftCode) { this.aircraftCode = aircraftCode; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getCabin() { return cabin; }
    public void setCabin(String cabin) { this.cabin = cabin; }
    public String getIncludedCheckedBags() { return includedCheckedBags; }
    public void setIncludedCheckedBags(String includedCheckedBags) { this.includedCheckedBags = includedCheckedBags; }

    public static Segment createSegment(JsonObject jo) {
            Segment f = new Segment();
        try {
                f.setId(jo.getString("id")); 
                f.setDepartureAirport(jo.getJsonObject("departure").getString("iataCode"));
                f.setDepartureDT(jo.getJsonObject("departure").getString("at"));
                f.setArrivalAirport(jo.getJsonObject("arrival").getString("iataCode"));
                f.setArrivalDT(jo.getJsonObject("arrival").getString("at"));
                f.setFlightNumber(jo.getString("carrierCode") + " " + jo.getString("number"));
                f.setOperatorCode(jo.getJsonObject("operating").getString("carrierCode"));
                f.setAircraftCode(jo.getJsonObject("aircraft").getString("code"));
                f.setDuration(jo.getString("duration"));
        } catch (Exception e) {
                e.printStackTrace();
        }
        return f;
    }

    public JsonObject toJson() {
        JsonObjectBuilder joBuilder = Json.createObjectBuilder();
        joBuilder
                .add("departure", Json.createObjectBuilder()
                        .add("iataCode", this.departureAirport)
                        .add("at", this.departureDT))
                .add("arrival", Json.createObjectBuilder()
                        .add("iataCode", this.arrivalAirport)
                        .add("at", this.arrivalDT));
        // seperate flightnumber into carriercode and number
        String[] flightNumberItems = this.flightNumber.split(" ");
        String carrierCode = flightNumberItems[0];
        String number = flightNumberItems[1];
        joBuilder
                .add("carrierCode", carrierCode)
                .add("number", number)
                .add("aircraft", Json.createObjectBuilder()
                        .add("code", this.aircraftCode))
                .add("operating", Json.createObjectBuilder()
                        .add("carrierCode", this.operatorCode))
                .add("duration", this.duration)
                .add("id", this.id);

        JsonObject jo = joBuilder.build();
        return jo;
    }

    @Override
    public String toString() {
        return "Segment [aircraftCode=" + aircraftCode + ", arrivalAirport=" + arrivalAirport + ", arrivalDT="
                + arrivalDT + ", cabin=" + cabin + ", departureAirport=" + departureAirport + ", departureDT="
                + departureDT + ", duration=" + duration + ", flightNumber=" + flightNumber + ", includedCheckedBags="
                + includedCheckedBags + ", operatorCode=" + operatorCode + "]";
    }
    
}
