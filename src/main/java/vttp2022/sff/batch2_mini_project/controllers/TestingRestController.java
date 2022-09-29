package vttp2022.sff.batch2_mini_project.controllers;

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
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp2022.sff.batch2_mini_project.models.FlightOffer;
import vttp2022.sff.batch2_mini_project.models.FlightOfferCart;
import vttp2022.sff.batch2_mini_project.repositories.FlightOfferRepository;

@RestController
@RequestMapping(path = "/testing", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestingRestController {

    @Autowired
    private FlightOfferRepository foRepo;

    @GetMapping ("{name}")
    public ResponseEntity<String> getTest(
            @PathVariable String name) {
        
        Optional<FlightOfferCart> opt = foRepo.getFOCart(name);
        
        if (opt.isEmpty()) {
			JsonObject payload = Json.createObjectBuilder()
				.add("error", "%s user not found.".formatted(name))
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
}