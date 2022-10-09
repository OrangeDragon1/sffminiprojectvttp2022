package vttp2022.sff.batch2_mini_project.models;

import jakarta.json.JsonObject;

public class UrbanArea {
    private String href;
    private String name;

    public UrbanArea(JsonObject jo) {
        this.href = jo.getString("href");
        this.name = jo.getString("name");
    }

    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
