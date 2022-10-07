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
    private String count;
    private static Integer counter = 1;
    private String origin;
    private String destination;
    private Boolean oneWay;
    private String departureDate;
    private String returnDate;
    private String travelClass;
    private String meta;
    private Boolean direct;

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
    public String getCount() { return count; }
    public void setCount(String count) { this.count = count; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Boolean getOneWay() { return oneWay; }
    public void setOneWay(Boolean oneWay) { this.oneWay = oneWay; }
    public String getDepartureDate() { return departureDate; }
    public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public String getTravelClass() { return travelClass; }
    public void setTravelClass(String travelClass) { this.travelClass = travelClass; }
    public String getMeta() { return meta; }
    public void setMeta(String meta) { this.meta = meta; }
    public Boolean getDirect() { return direct; }
    public void setDirect(Boolean direct) { this.direct = direct; }

    public static FlightOffer createFlightOffer(JsonObject jsonObject, Dictionaries d) {

        FlightOffer f = new FlightOffer();
        f.setTotalPrice(jsonObject.getJsonObject("price").getString("total"));
        f.setBasePrice(jsonObject.getJsonObject("price").getString("base"));
        f.setCurrency(jsonObject.getJsonObject("price").getString("currency"));
        f.setSurcharges(Double.parseDouble(f.getTotalPrice()) - Double.parseDouble(f.getBasePrice()));
        f.setCount("number" + counter);
        counter++;
        List<JsonObject> joList = new LinkedList<>();
        List<Itinerary> iList = new LinkedList<>();
        JsonArray jsonArray = jsonObject.getJsonArray("itineraries");
        jsonArray.forEach(jo -> joList.add((JsonObject) jo));
        joList.forEach(j -> iList.add(Itinerary.createItinerary(j)));
        f.setItineraryList(iList);
        JsonArray fareDetailsBySegmentsArray = jsonObject.getJsonArray("travelerPricings").getJsonObject(0).getJsonArray("fareDetailsBySegment");
        setCabinBaggageAircraftOperator(fareDetailsBySegmentsArray, f, d);
        f.setOrigin(f.getItineraryList().get(0).getSegmentList().get(0).getDepartureAirport());
        f.setDestination(f.getItineraryList().get(0).getSegmentList()
                .get(f.getItineraryList().get(0).getSegmentList().size()-1).getArrivalAirport());
        f.setOneWay(false);
        if (f.getItineraryList().size() <= 1) {
            f.setOneWay(true);
        }
        f.setDepartureDate(f.getItineraryList().get(0).getSegmentList().get(0).getDepartureDT().substring(0, 10));
        if (f.oneWay == true) {
            f.setReturnDate(null);
        } else {
            f.setReturnDate(f.getItineraryList().get(f.getItineraryList().size()-1).getSegmentList().get(0).getDepartureDT().substring(0, 10));
        }
        f.setTravelClass(f.getItineraryList().get(0).getSegmentList().get(0).getCabin());
        Boolean b = true;
        for (Itinerary itinerary : f.getItineraryList()) {
            b = b && itinerary.getDirect();
        }
        if (b == true) {
            f.setDirect(true);
        } else {
            f.setDirect(false);
        }
        if (f.oneWay == true) {
            f.setMeta(String.format("https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=%s&destinationLocationCode=%s&departureDate=%s&adults=1&travelClass=%s&nonStop=%b&currencyCode=%s", 
            f.getOrigin(), f.getDestination(), f.getDepartureDate(), f.getTravelClass(), f.getDirect(),f.getCurrency()));
        } else {
            f.setMeta(String.format("https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=%s&destinationLocationCode=%s&departureDate=%s&returnDate=%s&adults=1&travelClass=%s&nonStop=%b&currencyCode=%s", 
            f.getOrigin(), f.getDestination(), f.getDepartureDate(), f.getReturnDate(), f.getTravelClass(), f.getDirect(),f.getCurrency()));
        }
        return f;
    }

    public static FlightOffer createFlightOffer(JsonObject jsonObject) {

        FlightOffer f = new FlightOffer();
        f.setTotalPrice(jsonObject.getJsonObject("price").getString("total"));
        f.setBasePrice(jsonObject.getJsonObject("price").getString("base"));
        f.setCurrency(jsonObject.getJsonObject("price").getString("currency"));
        f.setSurcharges(Double.parseDouble(f.getTotalPrice()) - Double.parseDouble(f.getBasePrice()));
        f.setCount("number" + counter);
        counter++;
        List<JsonObject> joList = new LinkedList<>();
        List<Itinerary> iList = new LinkedList<>();
        JsonArray jsonArray = jsonObject.getJsonArray("itineraries");
        jsonArray.forEach(jo -> joList.add((JsonObject) jo));
        joList.forEach(j -> iList.add(Itinerary.createItinerary(j)));
        f.setItineraryList(iList);
        JsonArray fareDetailsBySegmentsArray = jsonObject.getJsonArray("travelerPricings").getJsonObject(0).getJsonArray("fareDetailsBySegment");
        setCabinBaggage(fareDetailsBySegmentsArray, f);
        f.setOrigin(f.getItineraryList().get(0).getSegmentList().get(0).getDepartureAirport());
        f.setDestination(f.getItineraryList().get(0).getSegmentList()
                .get(f.getItineraryList().get(0).getSegmentList().size()-1).getArrivalAirport());
        f.setOneWay(false);
        if (f.getItineraryList().size() <= 1) {
            f.setOneWay(true);
        }
        f.setDepartureDate(f.getItineraryList().get(0).getSegmentList().get(0).getDepartureDT().substring(0, 10));
        if (f.oneWay == true) {
            f.setReturnDate(null);
        } else {
            f.setReturnDate(f.getItineraryList().get(f.getItineraryList().size()-1).getSegmentList().get(0).getDepartureDT().substring(0, 10));
        }
        f.setTravelClass(f.getItineraryList().get(0).getSegmentList().get(0).getCabin());
        Boolean b = true;
        for (Itinerary itinerary : f.getItineraryList()) {
            b = b && itinerary.getDirect();
        }
        if (b == true) {
            f.setDirect(true);
        } else {
            f.setDirect(false);
        }
        if (f.oneWay == true) {
            f.setMeta(String.format("https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=%s&destinationLocationCode=%s&departureDate=%s&adults=1&travelClass=%s&nonStop=%b&currencyCode=%s", 
            f.getOrigin(), f.getDestination(), f.getDepartureDate(), f.getTravelClass(), f.getDirect(),f.getCurrency()));
        } else {
            f.setMeta(String.format("https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=%s&destinationLocationCode=%s&departureDate=%s&returnDate=%s&adults=1&travelClass=%s&nonStop=%b&currencyCode=%s", 
            f.getOrigin(), f.getDestination(), f.getDepartureDate(), f.getReturnDate(), f.getTravelClass(), f.getDirect(),f.getCurrency()));
        }
        return f;
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

    private static void setCabinBaggageAircraftOperator(JsonArray fareDetailsBySegmentsArray, FlightOffer f, Dictionaries d) {
        for (int j = 0; j < fareDetailsBySegmentsArray.size(); j++) {
            for (int k = 0; k < f.getItineraryList().size(); k++) {
                for (int n = 0; n < f.getItineraryList().get(k).getSegmentList().size(); n++) {
                    String segmentId = fareDetailsBySegmentsArray.getJsonObject(j).getString("segmentId");
                    String id = f.getItineraryList().get(k).getSegmentList().get(n).getId();
                    String segmentAircraftCode = f.getItineraryList().get(k).getSegmentList().get(n).getAircraftCode();
                    String segmentOperatorCode = f.getItineraryList().get(k).getSegmentList().get(n).getOperatorCode();
                    if (segmentId.equals(id)) {
                        String cabinString = fareDetailsBySegmentsArray.getJsonObject(j).getString("cabin");
                        f.getItineraryList().get(k).getSegmentList().get(n).setCabin(cabinString);
                        f.getItineraryList().get(k).getSegmentList().get(n).setAircraft(d.getAircraft().getString(segmentAircraftCode));
                        f.getItineraryList().get(k).getSegmentList().get(n).setOperator(d.getCarriers().getString(segmentOperatorCode));
                        JsonObject checkedBagsObject = fareDetailsBySegmentsArray.getJsonObject(j).getJsonObject("includedCheckedBags");
                        if (checkedBagsObject.containsKey("quantity")) {
                            String checkedBagString = String.valueOf(checkedBagsObject.getInt("quantity"));
                            f.getItineraryList().get(k).getSegmentList().get(n).setIncludedCheckedBags(checkedBagString + " CHECKED BAGGAGE ALLOWED");
                        } else if (checkedBagsObject.containsKey("weight")) {
                            String checkedBagString = checkedBagsObject.getInt("weight") + " " + checkedBagsObject.getString("weightUnit");
                            f.getItineraryList().get(k).getSegmentList().get(n).setIncludedCheckedBags(checkedBagString + " OF BAGGAGE ALLOWED");
                        }
                        break;
                    }
                }
            }
        }
    }

    private static void setCabinBaggage(JsonArray fareDetailsBySegmentsArray, FlightOffer f) {
        for (int j = 0; j < fareDetailsBySegmentsArray.size(); j++) {
            for (int k = 0; k < f.getItineraryList().size(); k++) {
                for (int n = 0; n < f.getItineraryList().get(k).getSegmentList().size(); n++) {
                    String segmentId = fareDetailsBySegmentsArray.getJsonObject(j).getString("segmentId");
                    String id = f.getItineraryList().get(k).getSegmentList().get(n).getId();
                    if (segmentId.equals(id)) {
                        String cabinString = fareDetailsBySegmentsArray.getJsonObject(j).getString("cabin");
                        f.getItineraryList().get(k).getSegmentList().get(n).setCabin(cabinString);
                        JsonObject checkedBagsObject = fareDetailsBySegmentsArray.getJsonObject(j).getJsonObject("includedCheckedBags");
                        if (checkedBagsObject.containsKey("quantity")) {
                            String checkedBagString = String.valueOf(checkedBagsObject.getInt("quantity"));
                            f.getItineraryList().get(k).getSegmentList().get(n).setIncludedCheckedBags(checkedBagString + " CHECKED BAGGAGE ALLOWED");
                        } else if (checkedBagsObject.containsKey("weight")) {
                            String checkedBagString = checkedBagsObject.getInt("weight") + " " + checkedBagsObject.getString("weightUnit");
                            f.getItineraryList().get(k).getSegmentList().get(n).setIncludedCheckedBags(checkedBagString + " OF BAGGAGE ALLOWED");
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
