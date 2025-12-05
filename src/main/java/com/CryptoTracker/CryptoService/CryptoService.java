package com.CryptoTracker.CryptoService;

import com.CryptoTracker.model.CryptoCoins;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoService {

    private final String API_URL="https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=usd&include_24hr_change=true";

    public List<CryptoCoins> getCryptoPrices(List<String> coins){
    List<CryptoCoins> coinsList=new ArrayList<>();

    String ids=String.join(",",coins);
    String url=String.format(API_URL,ids);

        RestTemplate restTemplate=new RestTemplate();
        String response=restTemplate.getForObject(url,String.class);

        JSONObject json=new JSONObject(response);

        for(String coin:coins){
            if(json.has(coin)){
                JSONObject coinJson=json.getJSONObject(coin);
                double price=coinJson.getDouble("usd");
                double change24h=coinJson.getDouble("usd_24h_change");
                coinsList.add(new CryptoCoins(coin,coin,price,change24h));

            }
        }
        return coinsList;

    }
}
