package vttp2022.sff.batch2_mini_project.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import vttp2022.sff.batch2_mini_project.models.FlightOffer;
import vttp2022.sff.batch2_mini_project.models.FlightOfferCart;
import vttp2022.sff.batch2_mini_project.repositories.FlightOfferRepository;
import vttp2022.sff.batch2_mini_project.services.FlightOfferService;

@Controller
@RequestMapping(path = "/trackedRoutes")
public class TrackedRoutesController {

    @Autowired
    private FlightOfferRepository foRepo;

    @Autowired
    private FlightOfferService foSvc;

    @PostMapping
    public String postTrackedRoutes(
            @RequestBody MultiValueMap<String, String> form,
            Model model) {

        String name = form.getFirst("name");
        if (name == null) {
            name = "UNKNOWN USER";
        }
        String upperName = name.toUpperCase();
        Optional<FlightOfferCart> opt = foRepo.getFOCart(upperName);

        if (opt.isEmpty()) {

            model.addAttribute("name", upperName);
            model.addAttribute("numberOfFO", 0);
            model.addAttribute("empty", true);

            return "trackedRoutes";
        }

        FlightOfferCart foCart = opt.get();
        Integer numberOfFO = foCart.getFOList().size();
        Boolean lastItem = false;
        if (foCart.getFOList().size() <= 1) {
            lastItem = true;
        }

        model.addAttribute("name", upperName);
        model.addAttribute("foList", foCart.getFOList());
        model.addAttribute("lastItem", lastItem);
        model.addAttribute("numberOfFO", numberOfFO);
        model.addAttribute("empty", false);

        return "trackedRoutes";
    }

    @PostMapping("/delete")
    public ModelAndView redirectedPostToPostDelete(
            HttpServletRequest request,
            @RequestBody MultiValueMap<String, String> form) {
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        String name = form.getFirst("name");
        String meta = form.getFirst("meta");

        if (name == null) {
            name = "UNKNOWN USER";
        }

        String upperName = name.toUpperCase();

        // need to fix null pointer exception
        Optional<FlightOfferCart> opt = foRepo.getFOCart(upperName);
        FlightOfferCart foCart = opt.get();
        List<FlightOffer> foList = foCart.getFOList();

        for (int i = 0; i < foList.size(); i++) {
            if (foList.get(i).getMeta().equalsIgnoreCase(meta)) {
                foList.remove(foList.get(i));
            }
        }

        foRepo.saveFOCart(FlightOfferCart.createCart(upperName, foList));

        return new ModelAndView("redirect:/trackedRoutes");
    }

    @PostMapping("/refresh")
    public ModelAndView redirectedPostToPostRefresh(
            HttpServletRequest request,
            @RequestBody MultiValueMap<String, String> form) {

        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        String name = form.getFirst("name");
        String meta = form.getFirst("meta");

        if (name == null) {
            name = "UNKNOWN USER";
        }
        
        String upperName = name.toUpperCase();

        // need to fix null pointer exception
        Optional<FlightOfferCart> opt = foRepo.getFOCart(upperName);
        FlightOfferCart foCart = opt.get();
        List<FlightOffer> foList = foCart.getFOList();
        // Grab from api using meta data
        // get index 0 because cheapest is at top of the array
        FlightOffer newFO = foSvc.getFlightOffers(meta).get(0);

        for (int i = 0; i < foList.size(); i++) {
            if (foList.get(i).getMeta().equalsIgnoreCase(meta)) {
                foList.remove(foList.get(i));
                foList.add(i, newFO);
            }
        }

        foRepo.saveFOCart(FlightOfferCart.createCart(upperName, foList));

        return new ModelAndView("redirect:/trackedRoutes");
    }

}
