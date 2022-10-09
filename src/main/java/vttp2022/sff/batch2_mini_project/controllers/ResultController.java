package vttp2022.sff.batch2_mini_project.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.sff.batch2_mini_project.models.Airport;
import vttp2022.sff.batch2_mini_project.models.FlightOffer;
import vttp2022.sff.batch2_mini_project.models.FlightOfferCart;
import vttp2022.sff.batch2_mini_project.repositories.FlightOfferRepository;
import vttp2022.sff.batch2_mini_project.services.FlightOfferService;

@Controller
@RequestMapping(path = "/result")
public class ResultController {

    @Autowired
    private FlightOfferRepository foRepo;

    @Autowired
    private FlightOfferService foSvc;

    @GetMapping
    public String getFlightOffer(
            @RequestParam MultiValueMap<String, String> form,
            Model model) {

        String nonStopString = form.getFirst("nonStop");
        String name = form.getFirst("name");

        if (name == null) {
            name = "UNKNOWN USER";
        }

        String upperName = name.toUpperCase();
        Boolean nonStopBoolean = true;

        if (nonStopString == null) {
            nonStopBoolean = false;
        }

        System.out.println(form.getFirst("originLocationCode"));
        List<FlightOffer> foList = foSvc.getFlightOffers(form.getFirst("originLocationCode"),
                form.getFirst("destinationLocationCode"),
                form.getFirst("departureDate"),
                form.getFirst("returnDate"),
                "1",
                form.getFirst("travelClass"),
                nonStopBoolean, form.getFirst("currencyCode"));

        Integer numberOfFO = 0;
        Optional<FlightOfferCart> opt = foRepo.getFOCart(upperName);

        // get cart size
        if (!opt.isEmpty()) {
            numberOfFO = opt.get().getFOList().size();
        }

        if (foList.isEmpty()) {

            model.addAttribute("name", upperName);
            model.addAttribute("numberOfFO", numberOfFO);
            model.addAttribute("empty", true);

            return "result";
        }

        List<Airport> airportList = getAirportList();
        Optional<Airport> foundAirport = airportList.stream()
                .filter(a -> a.getIataCode().equals(form.getFirst("destinationLocationCode").toUpperCase()))
                .findFirst();
        Airport airport = foundAirport.get();

        // cheapest flight information to be saved
        FlightOffer firstOffer = foList.get(0);

        // will not return null because search page requires input
        model.addAttribute("destination", airport.getMunicipality() + ", " + airport.getCountry());
        model.addAttribute("foList", foList);
        model.addAttribute("firstOffer", firstOffer.toJson().toString());
        model.addAttribute("name", upperName);
        model.addAttribute("numberOfFO", numberOfFO);
        model.addAttribute("empty", false);

        return "result";
    }

    public List<Airport> getAirportList() {

        String row;
        List<Airport> airportList = new LinkedList<>();

        try (BufferedReader csvReader = new BufferedReader(new FileReader("src/main/resources/static/airportsInfo.csv"))) {
            // read firstline, get rid of header
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                airportList.add(new Airport(
                    data[3].replaceAll("^\"|\"$", ""), 
                    data[4].replaceAll("^\"|\"$", ""), 
                    data[5].replaceAll("^\"|\"$", ""), 
                    data[7].replaceAll("^\"|\"$", ""), 
                    data[8].replaceAll("^\"|\"$", "")));
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader csvReader = new BufferedReader(new FileReader("src/main/resources/static/countriesInfo.csv"))) {
            // read firstline, get rid of header
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                for (Airport a : airportList) {
                    if (a.getIsoCountry().equals(data[1].replaceAll("^\"|\"$", ""))) {
                        a.setCountry(data[2].replaceAll("^\"|\"$", ""));
                    }
                }
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return airportList;
    }
}