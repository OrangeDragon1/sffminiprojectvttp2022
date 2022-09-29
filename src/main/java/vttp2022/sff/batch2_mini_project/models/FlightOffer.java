package vttp2022.sff.batch2_mini_project.models;

import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class FlightOffer {

    private List<Itinerary> itineraryList;
    private String totalPrice; 
    private String basePrice;
    private Double surcharges;
    private String currency;

    public List<Itinerary> getItineraryList() { return itineraryList; }
    public void setItineraryList(List<Itinerary> itineraryList) { this.itineraryList = itineraryList; }
    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }
    public String getBasePrice() { return basePrice; }
    public void setBasePrice(String basePrice) { this.basePrice = basePrice; }
    public Double getSurcharges() { return surcharges; }
    public void setSurcharges(Double surcharges) { this.surcharges = surcharges; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public static FlightOffer createFlightOffer(JsonObject jsonObject) {

        FlightOffer i = new FlightOffer();
        i.setTotalPrice(jsonObject.getJsonObject("price").getString("total"));
        i.setBasePrice(jsonObject.getJsonObject("price").getString("base"));
        i.setCurrency(jsonObject.getJsonObject("price").getString("currency"));
        i.setSurcharges(Double.parseDouble(i.getTotalPrice()) - Double.parseDouble(i.getBasePrice()));
        List<JsonObject> joList = new LinkedList<>();
        List<Itinerary> iList = new LinkedList<>();
        JsonArray jsonArray = jsonObject.getJsonArray("itineraries");
        jsonArray.forEach(jo -> joList.add((JsonObject) jo));
        joList.forEach(j -> iList.add(Itinerary.createItinerary(j)));
        i.setItineraryList(iList);
        JsonArray fareDetailsBySegmentsArray = jsonObject.getJsonArray("travelerPricings").getJsonObject(0).getJsonArray("fareDetailsBySegment");
        setCabinandBaggage(fareDetailsBySegmentsArray, i);
        return i;
    }

    public JsonObject toJson() {
        JsonObjectBuilder joBuilder = Json.createObjectBuilder();
        joBuilder
                .add("itineraries", toItineraryArray())
                .add("price", Json.createObjectBuilder()
                        .add("currency", this.currency)
                        .add("total", this.totalPrice)
                        .add("base", this.basePrice));
        joBuilder
                .add("travelerPricings", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder().add("fareDetailsBySegment", toTravelerPricingArray())));
        return joBuilder.build();
    }

    private JsonArray toItineraryArray() {
        JsonArrayBuilder jaBuilder = Json.createArrayBuilder();
        for (Itinerary i : itineraryList) {
            jaBuilder.add(i.toJson());
        }
        return jaBuilder.build();
    }

    // creates fareDetailsBySegment array
    private JsonArray toTravelerPricingArray() {
        JsonArrayBuilder jaBuilder = Json.createArrayBuilder();
        for (Itinerary i : itineraryList) {
            for (Segment s : i.getSegmentList()) {
                jaBuilder.add(toFareDetailsBySegmentObject(s));
            }
        }
        return jaBuilder.build();
    }
    
    // creates fareDetailsBySegmenet object
    private JsonObject toFareDetailsBySegmentObject(Segment s) {
        JsonObjectBuilder includedCheckedBagsBuilder = Json.createObjectBuilder();
        if (s.getIncludedCheckedBags().contains("CHECKED")) {
            Integer checkedBag = Integer.parseInt(s.getIncludedCheckedBags().split(" ")[0]);
            includedCheckedBagsBuilder
                    .add("quantity", checkedBag);
        } else {
            Integer weight = Integer.parseInt(s.getIncludedCheckedBags().split(" ")[0]);
            String weightUnit = s.getIncludedCheckedBags().split(" ")[1];
            includedCheckedBagsBuilder
                    .add("weight", weight)
                    .add("weightUnit", weightUnit);
        }
        JsonObject includedCheckedBags = includedCheckedBagsBuilder.build();
        return Json.createObjectBuilder()
                .add("segmentId", s.getId())
                .add("cabin", s.getCabin())
                .add("includedCheckedBags", includedCheckedBags)
                .build();
    }


    private static void setCabinandBaggage(JsonArray fareDetailsBySegmentsArray, FlightOffer i) {
        for (int j = 0; j < fareDetailsBySegmentsArray.size(); j++) {
            for (int k = 0; k < i.getItineraryList().size(); k++) {
                for (int n = 0; n < i.getItineraryList().get(k).getSegmentList().size(); n++) {
                    String segmentId = fareDetailsBySegmentsArray.getJsonObject(j).getString("segmentId");
                    String id = i.getItineraryList().get(k).getSegmentList().get(n).getId();
                    if (segmentId.equals(id)) {
                        String cabinString = fareDetailsBySegmentsArray.getJsonObject(j).getString("cabin");
                        i.getItineraryList().get(k).getSegmentList().get(n).setCabin(cabinString);

                        JsonObject checkedBagsObject = fareDetailsBySegmentsArray.getJsonObject(j).getJsonObject("includedCheckedBags");
                        if (checkedBagsObject.containsKey("quantity")) {
                            String checkedBagString = String.valueOf(checkedBagsObject.getInt("quantity"));
                            i.getItineraryList().get(k).getSegmentList().get(n).setIncludedCheckedBags(checkedBagString + " CHECKED BAGGAGE ALLOWED");
                        } else if (checkedBagsObject.containsKey("weight")) {
                            String checkedBagString = checkedBagsObject.getInt("weight") + " " + checkedBagsObject.getString("weightUnit");
                            i.getItineraryList().get(k).getSegmentList().get(n).setIncludedCheckedBags(checkedBagString + " OF BAGGAGE ALLOWED");
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "FlightOffer [basePrice=" + basePrice + ", itineraryList=" + itineraryList + ", surcharges=" + surcharges
                + ", totalPrice=" + totalPrice + "]";
    }
}