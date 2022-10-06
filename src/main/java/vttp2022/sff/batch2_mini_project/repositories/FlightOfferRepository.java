package vttp2022.sff.batch2_mini_project.repositories;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.sff.batch2_mini_project.models.FlightOffer;
import vttp2022.sff.batch2_mini_project.models.FlightOfferCart;

@Repository
public class FlightOfferRepository {
    
    @Autowired
    @Qualifier("redislab")
    private RedisTemplate<String, String> redisTemplate;

    public void saveFOCart(FlightOfferCart foCart) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();

        if (!redisTemplate.hasKey(foCart.getName())) {
            listOps.leftPush(foCart.getName(), toJson(foCart.getFOList()).toString());
            return;
        }
        redisTemplate.delete(foCart.getName());
        listOps.leftPush(foCart.getName(), toJson(foCart.getFOList()).toString());
    }

    public Optional<FlightOfferCart> getFOCart(String name) {

        if (!redisTemplate.hasKey(name)) {
			return Optional.empty(); 
        }

        ListOperations<String, String> listOps = redisTemplate.opsForList();
        String payload = "";
        long size = listOps.size(name);
		for (long i = 0; i < size; i++) {
            payload = listOps.index(name, i);
		}

        List<JsonObject> joList = getAvailableFlightOffer(payload);
        List<FlightOffer> fList = new LinkedList<>();
        for (JsonObject jo : joList) {
            fList.add(FlightOffer.createFlightOffer(jo));
        }
        FlightOfferCart foCart = FlightOfferCart.createCart(name, fList);
        return Optional.of(foCart);
    }

    public Boolean deleteKey(String name) {

        if (!redisTemplate.hasKey(name)) {
			return false;
        }

        redisTemplate.delete(name);
        return true;
    }

    private List<JsonObject> getAvailableFlightOffer(String payload) {

        Reader stringReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(stringReader);
        JsonObject payloadObject = jsonReader.readObject();
        JsonArray dataArray = payloadObject.getJsonArray("data");

        List<JsonObject> joList = new LinkedList<>();
        dataArray.forEach(jo -> joList.add((JsonObject) jo));

        return joList;
    }

    private JsonObject toJson(List<FlightOffer> foList) {

        JsonArrayBuilder dataArray = Json.createArrayBuilder();
        for (FlightOffer fo : foList) {
            dataArray.add(fo.toJson());
        }
        JsonObject payload = Json.createObjectBuilder()
                .add("data", dataArray.build())
                .build();
        
        return payload;
    }
}
