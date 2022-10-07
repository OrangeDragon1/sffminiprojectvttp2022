package vttp2022.sff.batch2_mini_project.controllers;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
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
    String upperName = name.toUpperCase();
    String firstOffer = form.getFirst("firstOffer");
    FlightOfferCart foCart;

    Optional<FlightOfferCart> opt = foRepo.getFOCart(upperName);
    if(!opt.isEmpty()) {
        foCart = opt.get();    
    } else {
        foCart = FlightOfferCart.createCart(upperName);
    }

    // create flightoffer object and add to cart
    Reader stringReader = new StringReader(firstOffer);
    JsonReader jsonReader = Json.createReader(stringReader);
    JsonObject firstOfferObject = jsonReader.readObject();
    FlightOffer flightOffer = FlightOffer.createFlightOffer(firstOfferObject);
    String meta = flightOffer.getMeta();
    Boolean exist = false;
    Integer numberOfFO = foCart.getFOList().size();

    // prevent duplication
    List<FlightOffer> foList = foCart.getFOList();
    for (int i = 0; i < foList.size(); i++) {
        if (foList.get(i).getMeta().equalsIgnoreCase(meta)) {
            exist = true;
        }
    }

    if (exist) {

        model.addAttribute("saved", false);
        model.addAttribute("name", upperName);
        model.addAttribute("numberOfFO", numberOfFO);

        return "save";
    }

    foCart.addFlightOffer(flightOffer);
    foRepo.saveFOCart(foCart);
    numberOfFO = foCart.getFOList().size();

    model.addAttribute("saved", true);
    model.addAttribute("name", upperName);
    model.addAttribute("numberOfFO", numberOfFO);

    return "save";
    }
}
