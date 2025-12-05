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
    public String index(){
        return "index";
    }

    @PostMapping("/track")
    public String track(@RequestParam String coins, Model model){
        List<String> coinList= Arrays.asList(coins.split(","));
        List<CryptoCoins>result=cryptoService.getCryptoPrices(coinList);
        model.addAttribute("coins",result);
        return "result";
    }

}
