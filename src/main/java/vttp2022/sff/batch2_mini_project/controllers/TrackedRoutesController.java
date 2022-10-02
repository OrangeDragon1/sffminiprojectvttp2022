package vttp2022.sff.batch2_mini_project.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import vttp2022.sff.batch2_mini_project.models.FlightOfferCart;
import vttp2022.sff.batch2_mini_project.repositories.FlightOfferRepository;

@Controller
@RequestMapping(path = "/trackedRoutes")
public class TrackedRoutesController {

    @Autowired
    private FlightOfferRepository foRepo;

    @PostMapping
    public String postTrackedRoutes(
            @RequestBody MultiValueMap<String, String> form,
            Model model) {

        String name = form.getFirst("name");
        String upperName = name.toUpperCase();

        Optional<FlightOfferCart> opt = foRepo.getFOCart(upperName);

        FlightOfferCart foCart = opt.get();
        System.out.println(foCart.getFOList().get(0).getMeta());
        
        model.addAttribute("name", name.toUpperCase());
        model.addAttribute("foList", foCart.getFOList());

        return "trackedRoutes";
    }
}
