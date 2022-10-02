package vttp2022.sff.batch2_mini_project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.sff.batch2_mini_project.models.FlightOffer;
import vttp2022.sff.batch2_mini_project.services.FlightOfferService;

@Controller
@RequestMapping(path = "/result")
public class ResultController {

    @Autowired
    private FlightOfferService foSvc;
    private static List<FlightOffer> foList;

    @GetMapping
    public String getFlightOffer(
            @RequestParam MultiValueMap<String, String> form,
            Model model) {

        
        String nonStopString = form.getFirst("nonStop");
        Boolean nonStopBoolean = true;

        if (nonStopString == null) {
            nonStopBoolean = false;
        }
        
        foList = foSvc.getFlightOffers(form.getFirst("originLocationCode"),
                form.getFirst("destinationLocationCode"), 
                form.getFirst("departureDate"), 
                form.getFirst("returnDate"),
                form.getFirst("travelClass"),
                "1", 
                "SGD", 
                nonStopBoolean);

        FlightOffer firstOffer = foList.get(0);
        System.out.println(foList.get(0).toString());

        model.addAttribute("origin", form.getFirst("originLocationCode").toUpperCase());
        model.addAttribute("destination", form.getFirst("destinationLocationCode").toUpperCase());
        model.addAttribute("foList", foList);
        model.addAttribute("firstOffer", firstOffer.toJson().toString()); 
        model.addAttribute("name", form.getFirst("name"));

        return "result";
    }
}