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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vttp2022.sff.batch2_mini_project.models.Airport;
import vttp2022.sff.batch2_mini_project.models.FlightOfferCart;
import vttp2022.sff.batch2_mini_project.models.LabelValue;
import vttp2022.sff.batch2_mini_project.repositories.FlightOfferRepository;

@Controller
@RequestMapping(path = "/search")
public class SearchController {

    @Autowired
    private FlightOfferRepository foRepo;

    List<Airport> airportList = getAirportList();

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
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
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

    @GetMapping("/airportNamesAutoComplete")
    @ResponseBody
    public List<LabelValue> airportNamesAutoComplete(
            @RequestParam(value = "term", required = false, defaultValue = "") String term) {
        
        List<LabelValue> suggestions = new LinkedList<>();
        for (Airport a : airportList) {
            if (a.getName().toLowerCase().contains(term.toLowerCase()) || a.getCountry().toLowerCase().contains(term.toLowerCase()) || a.getMunicipality().toLowerCase().contains(term.toLowerCase()) || a.getIataCode().toLowerCase().contains(term.toLowerCase()) ) {
                LabelValue labelValue = new LabelValue();
                labelValue.setLabel("(" + a.getIataCode() + ") " + a.getName());
                labelValue.setValue(a.getIataCode());
                labelValue.setDesc(a.getMunicipality() + ", " + a.getCountry());
                suggestions.add(labelValue);
                
            }
        }

        return suggestions;
    }

}
