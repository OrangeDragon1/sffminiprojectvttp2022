package vttp2022.sff.batch2_mini_project.services;

import java.io.Reader;
import java.io.StringReader;
import java.lang.StackWalker.Option;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.sff.batch2_mini_project.models.UrbanArea;

@Service
public class ImageService {

    private final String URBAN_AREAS = "https://api.teleport.org/api/urban_areas/";

    public Optional<String> getImage(String cityName) {

        String url;
        String payload = "";

        try {
            url = UriComponentsBuilder.fromUriString(URBAN_AREAS).toUriString();
            RequestEntity<Void> requestEntity = RequestEntity
                    .get(url)
                    .build();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> respEntity = restTemplate.exchange(requestEntity, String.class);
            payload = respEntity.getBody();

        } catch (Exception e) {
            System.err.printf("Error: %s\n", e.getMessage());
            // return Collections.emptyList();
            return Optional.empty();
        }

        Reader stringReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(stringReader);
        JsonObject payloadObject = jsonReader.readObject();
        JsonArray uaArray = payloadObject.getJsonObject("_links").getJsonArray("ua:item");

        List<JsonObject> joList = new LinkedList<>();
        List<UrbanArea> uaList = new LinkedList<>();

        uaArray.forEach(jo -> joList.add((JsonObject) jo));
        joList.forEach(j -> uaList.add(new UrbanArea(j)));

        Optional<UrbanArea> opt = getUrbanArea(cityName, uaList);

        if (opt.isEmpty()) {
            return Optional.empty();
        }

        String uaUrl = opt.get().getHref();
        url = UriComponentsBuilder.fromUriString(uaUrl).toUriString();
        RequestEntity<Void> requestEntity = RequestEntity
                .get(url)
                .build();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> respEntity = restTemplate.exchange(requestEntity, String.class);
        payload = respEntity.getBody();
        stringReader = new StringReader(payload);
        jsonReader = Json.createReader(stringReader);
        payloadObject = jsonReader.readObject();
        String imagesUrl = "";

        try {
            imagesUrl = payloadObject.getJsonObject("_links").getJsonObject("ua:images").getString("href");
        } catch (Exception e) {
            e.printStackTrace();
        }

        url = UriComponentsBuilder.fromUriString(imagesUrl).toUriString();
        requestEntity = RequestEntity
                .get(url)
                .build();
        restTemplate = new RestTemplate();
        respEntity = restTemplate.exchange(requestEntity, String.class);
        payload = respEntity.getBody();
        stringReader = new StringReader(payload);
        jsonReader = Json.createReader(stringReader);
        payloadObject = jsonReader.readObject();
        String imageLink = "";

        try {
            imageLink = payloadObject.getJsonArray("photos").getJsonObject(0).getJsonObject("image").getString("web");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.of(imageLink);
    }

    private Optional<UrbanArea> getUrbanArea(String cityName, List<UrbanArea> uaList) {

        for (UrbanArea ua : uaList) {
            if (ua.getName().toLowerCase().equals(cityName.toLowerCase())) {
                return Optional.of(ua);
            }
        }

        return Optional.empty();

    }
}
