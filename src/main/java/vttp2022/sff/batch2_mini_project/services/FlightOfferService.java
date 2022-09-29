package vttp2022.sff.batch2_mini_project.services;

import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.sff.batch2_mini_project.models.FlightOffer;

@Service
public class FlightOfferService {
        
    @Value("${CLIENT_ID}")
    private String clientID;
    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    private static final String FLIGHT_OFFERS_URL = "https://test.api.amadeus.com/v2/shopping/flight-offers";

    public List<FlightOffer> getFlightOffers(String originLocationCode, String destinationLocationCode, String departureDate, 
            String returnDate, String travelClass, String adults, String currencyCode, Boolean nonStop) {

        String payload; 
        String url;

        // nonStop=true

        try {
            if (returnDate == null) {
                url = UriComponentsBuilder.fromUriString(FLIGHT_OFFERS_URL)
                    .queryParam("originLocationCode", originLocationCode)
                    .queryParam("destinationLocationCode", destinationLocationCode)
                    .queryParam("departureDate", departureDate)
                    .queryParam("travelClass", travelClass)
                    .queryParam("adults", adults)
                    .queryParam("currencyCode", currencyCode)
                    .queryParam("nonStop", nonStop)
                    .toUriString();
            } else {
                url = UriComponentsBuilder.fromUriString(FLIGHT_OFFERS_URL)
                    .queryParam("originLocationCode", originLocationCode)
                    .queryParam("destinationLocationCode", destinationLocationCode)
                    .queryParam("departureDate", departureDate)
                    .queryParam("returnDate", returnDate)
                    .queryParam("travelClass", travelClass)
                    .queryParam("adults", adults)
                    .queryParam("currencyCode", currencyCode)
                    .queryParam("nonStop", nonStop)
                    .toUriString();
            }
            
            RequestEntity<Void> requestEntity = RequestEntity
                    .get(url)
                    .header("Authorization", getAccessToken())
                    .build();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> respEntity = restTemplate.exchange(requestEntity, String.class);
            payload = respEntity.getBody();

        } catch (Exception e) {
            System.err.printf("Error: %s\n", e.getMessage());
            return Collections.emptyList();
        }

        List<JsonObject> joList = getAvailableFlightOffer(payload);
        List<FlightOffer> fList = new LinkedList<>();
        for (JsonObject jo : joList) {
            fList.add(FlightOffer.createFlightOffer(jo));
        }

        return fList;
    }

    // method to get Authorization token
    private String getAccessToken() throws OAuthSystemException, OAuthProblemException {
        
        String tokenURL = "https://test.api.amadeus.com/v1/security/oauth2/token";
        String encodedValue = Base64.getEncoder().encodeToString((clientID + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        OAuthClient client = new OAuthClient(new URLConnectionClient());

        OAuthClientRequest request = OAuthClientRequest.tokenLocation(tokenURL)
                .setGrantType(GrantType.CLIENT_CREDENTIALS)
                .buildBodyMessage();

        request.addHeader("Authorization", "Basic " + encodedValue);
        OAuthJSONAccessTokenResponse oAuthResponse = client.accessToken(request, OAuth.HttpMethod.POST, OAuthJSONAccessTokenResponse.class);

        return "Bearer " + oAuthResponse.getAccessToken();
    }

    // method to get payloadObject
    private List<JsonObject> getAvailableFlightOffer(String payload) {

        Reader stringReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(stringReader);
        JsonObject payloadObject = jsonReader.readObject();
        JsonArray dataArray = (JsonArray) payloadObject.get("data");

        // System.out.println(payloadObject.get("data").getValueType());
        List<JsonObject> joList = new LinkedList<>();
        dataArray.forEach(jo -> joList.add((JsonObject) jo));

        return joList;
    }
}
