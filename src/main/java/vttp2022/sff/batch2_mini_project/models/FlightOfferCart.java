package vttp2022.sff.batch2_mini_project.models;

import java.util.LinkedList;
import java.util.List;

public class FlightOfferCart {
    private String name;
    private List<FlightOffer> foList;

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public List<FlightOffer> getFOList() { return this.foList; } 
    public void setFOList(List<FlightOffer> foList) { this.foList = foList; }

    public static FlightOfferCart createCart(String name) {
        FlightOfferCart foCart = new FlightOfferCart();
        foCart.setName(name);
        foCart.setFOList(new LinkedList<>());
        return foCart;
    }

    public static FlightOfferCart createCart(String name, List<FlightOffer> foList) {
        FlightOfferCart foCart = new FlightOfferCart();
        foCart.setName(name);
        foCart.setFOList(foList);
        return foCart;
    }

    public void addFlightOffer(FlightOffer fo) {
        this.foList.add(fo);
    }
}
