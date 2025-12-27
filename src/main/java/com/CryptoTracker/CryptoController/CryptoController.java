package com.CryptoTracker.CryptoController;

import com.CryptoTracker.CryptoService.CryptoService;
import com.CryptoTracker.model.CryptoCoins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class CryptoController {

    @Autowired
    private CryptoService cryptoService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/track")
    public String track(@RequestParam String coins, Model model) {
        try {
            List<String> coinList = Arrays.asList(coins.split(","));
            List<CryptoCoins> result = cryptoService.getCryptoPrices(coinList);
            model.addAttribute("coins", result);
            if (result.isEmpty()) {
                model.addAttribute("error", "No data found for the given coins. Please check the names and try again.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred while fetching prices.");
            model.addAttribute("coins", List.of()); // Ensure coins list is not null
        }
        return "result";
    }

}
