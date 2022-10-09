package vttp2022.sff.batch2_mini_project.models;

public class Airport {
    private String name;
    private String continent;
    private String isoCountry;
    private String municipality;
    private String iataCode;
    private String country;

    public Airport(String name, String continent, String isoCountry, String municipality, String iataCode) {
        this.name = name;
        this.continent = continent;
        this.isoCountry = isoCountry;
        this.municipality = municipality;
        this.iataCode = iataCode;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }
    public String getIsoCountry() { return isoCountry; }
    public void setIsoCountry(String isoCountry) { this.isoCountry = isoCountry; }
    public String getMunicipality() { return municipality; }
    public void setMunicipality(String municipality) { this.municipality = municipality; }
    public String getIataCode() { return iataCode; }
    public void setIataCode(String iataCode) { this.iataCode = iataCode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    @Override
    public String toString() {
        return "Airport [name=" + name + ", continent=" + continent + ", isoCountry=" + isoCountry + ", municipality="
                + municipality + ", iataCode=" + iataCode + ", country=" + country + "]";
    }

}
