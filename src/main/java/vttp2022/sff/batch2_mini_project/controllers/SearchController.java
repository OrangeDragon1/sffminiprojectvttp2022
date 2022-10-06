package vttp2022.sff.batch2_mini_project.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import vttp2022.sff.batch2_mini_project.models.Airport;
import vttp2022.sff.batch2_mini_project.models.FlightOfferCart;
import vttp2022.sff.batch2_mini_project.repositories.FlightOfferRepository;

@Controller
@RequestMapping(path = "/search")
public class SearchController {

    @Autowired
    private FlightOfferRepository foRepo;

    @PostMapping
    public String postCart( 
            @RequestBody MultiValueMap<String, String> form, 
            Model model) {
        
        String dateNow = java.time.LocalDate.now().toString();
        String dateLater = java.time.LocalDate.now().plusYears(1).toString();
        String name = form.getFirst("name");

        if (name == null) {
            name = "UNKNOWN USER";
        }

        String upperName = name.toUpperCase().trim();
        Integer numberOfFO = 0;
        Optional<FlightOfferCart> opt = foRepo.getFOCart(upperName);

        if (!opt.isEmpty()) {
            numberOfFO = opt.get().getFOList().size();
        } 
        
        List<Airport> airportList = getAirportList().stream()
                .sorted((o1, o2) -> o1.getCity().compareTo(o2.getCity()))
                .collect(Collectors.toList());

        model.addAttribute("name", upperName);
        model.addAttribute("dateNow", dateNow);
        model.addAttribute("dateLater", dateLater);
        model.addAttribute("numberOfFO", numberOfFO);
        model.addAttribute("airportList", airportList);

        return "search";
    }

    public List<Airport> getAirportList() {

        String row;
        List<Airport> airportList = new LinkedList<>();

        try (BufferedReader csvReader = new BufferedReader(new FileReader("src/main/resources/static/airports.csv"))) {
            // read firstline, get rid of header
            csvReader.readLine();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                airportList.add(new Airport(data[0], data[1], data[2], data[3], data[5]));
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return airportList;
    }
}
