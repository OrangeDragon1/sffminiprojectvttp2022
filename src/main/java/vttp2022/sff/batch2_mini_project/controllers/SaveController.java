package vttp2022.sff.batch2_mini_project.controllers;

import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.sff.batch2_mini_project.models.FlightOffer;
import vttp2022.sff.batch2_mini_project.models.FlightOfferCart;
import vttp2022.sff.batch2_mini_project.repositories.FlightOfferRepository;

@Controller
@RequestMapping (path = "/save")
public class SaveController {
    
    @Autowired
    private FlightOfferRepository foRepo;
    
    @PostMapping
    public String postSaveFO(
            @RequestBody MultiValueMap<String, String> form,
            Model model) {
    
    String name = form.getFirst("name");
    String firstOffer = form.getFirst("firstOffer");
    FlightOfferCart foCart;

    Optional<FlightOfferCart> opt = foRepo.getFOCart(name);
    if(!opt.isEmpty()) {
        foCart = opt.get();    
    } else {
        foCart = FlightOfferCart.createCart(name);
    }

    // create flightoffer object and add to cart
    Reader stringReader = new StringReader(firstOffer);
    JsonReader jsonReader = Json.createReader(stringReader);
    JsonObject firstOfferObject = jsonReader.readObject();
    foCart.addFlightOffer(FlightOffer.createFlightOffer(firstOfferObject));

    System.out.println(firstOfferObject.toString());
    foRepo.saveFOCart(foCart);
    model.addAttribute("name", name);

    return "save";
    }
}
