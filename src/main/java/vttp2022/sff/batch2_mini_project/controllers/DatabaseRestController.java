package vttp2022.sff.batch2_mini_project.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp2022.sff.batch2_mini_project.models.FlightOffer;
import vttp2022.sff.batch2_mini_project.models.FlightOfferCart;
import vttp2022.sff.batch2_mini_project.repositories.FlightOfferRepository;
import vttp2022.sff.batch2_mini_project.services.ImageService;

@RestController
@RequestMapping(path = "/grabUser", produces = MediaType.APPLICATION_JSON_VALUE)
public class DatabaseRestController {

    @Autowired
    private FlightOfferRepository foRepo;

    @GetMapping("{name}")
    public ResponseEntity<String> getIndividualUser(
            @PathVariable String name) {

        String upperName = name.toUpperCase();
        Optional<FlightOfferCart> opt = foRepo.getFOCart(upperName);

        if (opt.isEmpty()) {
            JsonObject payload = Json.createObjectBuilder()
                    .add("error", "User [%s] does not exist in the database.".formatted(upperName))
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(payload.toString());
        }

        FlightOfferCart foCart = opt.get();

        JsonArrayBuilder dataArray = Json.createArrayBuilder();
        for (FlightOffer fo : foCart.getFOList()) {
            dataArray.add(fo.toJson());
        }

        JsonObject payload = Json.createObjectBuilder()
                .add("data", dataArray.build())
                .build();

        return ResponseEntity.ok(payload.toString());

    }

    @GetMapping("/totalNumber")
    public ResponseEntity<String> getTotalNumber() {
        
        List<String> keyList = foRepo.getAllUsers();
        List<String> keyAndNumberOfObject = new LinkedList<>();
        for (String s : keyList) {
            Optional<FlightOfferCart> opt = foRepo.getFOCart(s);
            FlightOfferCart foCart = opt.get();
            keyAndNumberOfObject.add(s + "," + foCart.getFOList().size());
        }
        
        if(keyList.isEmpty()) {
            JsonObject payload = Json.createObjectBuilder()
                    .add("error", "There are currently no users in the database.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(payload.toString());
        }

        JsonArrayBuilder dataArray = Json.createArrayBuilder();

        for (String s : keyAndNumberOfObject) {
            String[] dataItem = s.split(",");
            JsonObject jo = Json.createObjectBuilder()
                    .add("username", dataItem[0])
                    .add("fo_in_cart", Integer.parseInt(dataItem[1]))
                    .build();
            dataArray.add(jo);
        }
        JsonArray jsonArray = dataArray.build();

        return ResponseEntity.ok(Json.createObjectBuilder()
                .add("total_users", keyList.size())
                .add("data", jsonArray)
                .build().toString());
    }
}