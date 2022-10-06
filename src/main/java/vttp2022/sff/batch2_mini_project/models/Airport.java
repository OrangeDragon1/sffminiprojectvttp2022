package vttp2022.sff.batch2_mini_project.models;

public class Airport {
    private String icao;
    private String iata;
    private String name;
    private String city;
    private String country;

    public String getIcao() { return icao; }
    public void setIcao(String icao) { this.icao = icao; }
    public String getIata() { return iata; }
    public void setIata(String iata) { this.iata = iata; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Airport(String icao, String iata, String name, String city, String country) {
        this.icao = icao;
        this.iata = iata;
        this.name = name;
        this.city = city;
        this.country = country;
    }
    @Override
    public String toString() {
        return "Airport [icao=" + icao + ", iata=" + iata + ", name=" + name + ", city=" + city + ", country=" + country
                + "]";
    }
}
