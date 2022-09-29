package vttp2022.sff.batch2_mini_project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp2022.sff.batch2_mini_project.models.FlightOffer;
import vttp2022.sff.batch2_mini_project.services.FlightOfferService;

@RestController
@RequestMapping(path = "/grab", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItineraryRestController {

    @Autowired
    private FlightOfferService foSvc;

    @GetMapping(path = "/flightOffers")
    public ResponseEntity<String> getFlightOffers() {

        // List<FlightOffer> fList = foSvc.getFlightOffers("SIN", 
        //         "ICN", 
        //         "2022-11-01", 
        //         "2022-11-06", 
        //         "BUSINESS", 
        //         "1");
        
        // System.out.println(fList.get(0).getItineraryList().get(0).getSegmentList().get(0).getClass());
        
        return null;
    }
}
