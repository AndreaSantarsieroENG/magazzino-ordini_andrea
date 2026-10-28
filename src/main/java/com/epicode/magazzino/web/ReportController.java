package com.epicode.magazzino.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

/**
 * Modulo report consegnato dal fornitore esterno.
 * Funziona, ma nessuno lo ha mai rivisto.
 */
@Controller
@RequestMapping("/report")
public class ReportController {

    private final JdbcTemplate jdbc;

    public ReportController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Ordini di un cliente: /report/ordini?cliente=Rossi */
    @GetMapping("/ordini")
    public String ordini(@RequestParam String cliente, Model model) {
        model.addAttribute("righe", jdbc.queryForList(
                "SELECT id, cliente, stato FROM ordini WHERE cliente = ?", cliente));
        model.addAttribute("titolo", "Ordini di " + cliente);
        return "report";
    }

    /** Scarica un report già generato: /report/file?nome=ordini-ottobre.csv */
    @GetMapping("/file")
    @ResponseBody
    public byte[] file(@RequestParam String nome) {
        try {
            Path directory = Path.of("report").toRealPath();
            Path file = directory.resolve(nome).normalize();
            if (!file.startsWith(directory)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            Path realFile = file.toRealPath();
            if (!realFile.startsWith(directory) || !Files.isRegularFile(realFile)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return Files.readAllBytes(realFile);
        } catch (IOException | InvalidPathException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Report non trovato", e);
        }
    }

    /** Intestazione con segnaposto: /report/intestazione?modello=Report del ${date:dd/MM/yyyy} */
    @GetMapping(value = "/intestazione", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String intestazione(@RequestParam String modello) {
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ITALY));
        return HtmlUtils.htmlEscape(modello.replace("${date:dd/MM/yyyy}", data));
    }
}
