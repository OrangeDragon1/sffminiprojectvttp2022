package vttp2022.sff.batch2_mini_project.models;

import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Itinerary {

    private List<Segment> segmentList;
    private String totalDuration;
    private Boolean direct;

    public List<Segment> getSegmentList() { return segmentList; }
    public void setSegmentList(List<Segment> segmentList) { this.segmentList = segmentList; }
    public String getTotalDuration() { return totalDuration; }
    public void setTotalDuration(String totalDuration) { this.totalDuration = totalDuration; }
    public Boolean getDirect() { return direct; }
    public void setDirect(Boolean direct) { this.direct = direct; }

    public static Itinerary createItinerary(JsonObject jsonObject) { 
        Itinerary f = new Itinerary();
        f.setTotalDuration(jsonObject.getString("duration"));
        List<JsonObject> joList = new LinkedList<>();
        List<Segment> fList = new LinkedList<>();
        JsonArray jsonArray = jsonObject.getJsonArray("segments");
        jsonArray.forEach(jo -> joList.add((JsonObject)jo));
        joList.forEach(j -> fList.add(Segment.createSegment(j)));
        f.setSegmentList(fList);
        f.setDirect(false);
        if (f.getSegmentList().size() <= 1) {
            f.setDirect(true);
        }
        return f;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("duration", this.totalDuration)
                .add("segments", toSegmentArray())
                .build();
    }

    private JsonArray toSegmentArray() {
        JsonArrayBuilder jaBuilder = Json.createArrayBuilder();
        for (Segment s : segmentList) {
            jaBuilder.add(s.toJson());
        }
        return jaBuilder.build();
    }

    @Override
    public String toString() {
        return "Itinerary [SegmentList=" + segmentList + ", totalDuration=" + totalDuration + "]";
    }
    
}
