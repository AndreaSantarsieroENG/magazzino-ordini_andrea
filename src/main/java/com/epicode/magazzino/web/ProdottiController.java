package com.epicode.magazzino.web;

import com.epicode.magazzino.model.Prodotto;
import com.epicode.magazzino.service.CatalogoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProdottiController {

    private final CatalogoService catalogo;

    public ProdottiController(CatalogoService catalogo) {
        this.catalogo = catalogo;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/prodotti";
    }

    @GetMapping("/prodotti")
    public String elenco(Model model) {
        model.addAttribute("prodotti", catalogo.elenco());
        return "prodotti";
    }

    @PostMapping("/api/prodotti")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Prodotto creaProdotto(@RequestBody ProdottoForm form) {
        return catalogo.crea(form.getCodice(), form.getNome(), form.getPrezzo(), form.getDisponibili(), form.getScortaMinima());
    }
}
