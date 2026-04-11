package com.CryptoTracker.CryptoService;

import com.CryptoTracker.model.CryptoCoins;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoService {

    private final String API_URL = "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=usd&include_24hr_change=true";

    public List<CryptoCoins> getCryptoPrices(List<String> coins) {
        List<CryptoCoins> coinsList = new ArrayList<>();
        if (coins == null || coins.isEmpty()) {
            return coinsList;
        }

        // Normalize inputs: trim and lowercase
        List<String> normalizedCoins = new ArrayList<>();
        for (String c : coins) {
            if (c != null && !c.trim().isEmpty()) {
                normalizedCoins.add(c.trim().toLowerCase());
            }
        }

        if (normalizedCoins.isEmpty()) {
            return coinsList;
        }

        String ids = String.join(",", normalizedCoins);
        String url = String.format(API_URL, ids);

        try {
            RestTemplate restTemplate = new RestTemplate();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.set("Accept", "application/json");
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
            
            org.springframework.http.ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url, 
                    org.springframework.http.HttpMethod.GET, 
                    entity, 
                    String.class
            );
            
            String response = responseEntity.getBody();

            if (response != null) {
                JSONObject json = new JSONObject(response);

                for (String coin : normalizedCoins) {
                    if (json.has(coin)) {
                        JSONObject coinJson = json.getJSONObject(coin);
                        double price = coinJson.getDouble("usd");
                        double change24h = coinJson.optDouble("usd_24h_change", 0.0);
                        coinsList.add(new CryptoCoins(coin, coin, price, change24h));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coinsList;
    }
}
