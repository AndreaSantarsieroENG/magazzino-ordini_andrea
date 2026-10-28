package com.epicode.magazzino.web;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epicode.magazzino.service.NuovoOrdine;
import com.epicode.magazzino.service.OrdineService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReportSicurezzaTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrdineService ordini;

    @Test
    void filtraOrdiniSenzaInterpretareIlParametroComeSql() throws Exception {
        ordini.crea(new NuovoOrdine("Cliente Sicuro",
                List.of(new NuovoOrdine.Riga("GUA-L", 1))));

        mvc.perform(get("/report/ordini").param("cliente", "' OR '1'='1"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Cliente Sicuro"))));

        mvc.perform(get("/report/ordini").param("cliente", "Cliente Sicuro"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Cliente Sicuro")));
    }

    @Test
    void intestazioneNonInterpretaHtmlInseritoDalCliente() throws Exception {
        mvc.perform(get("/report/ordini").param("cliente", "<script>alert(1)</script>"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("&lt;script&gt;alert(1)&lt;/script&gt;")))
                .andExpect(content().string(not(containsString("<script>"))));
    }

    @Test
    void downloadNonConsenteDiUscireDallaDirectoryReport() throws Exception {
        mvc.perform(get("/report/file").param("nome", "../pom.xml"))
                .andExpect(status().isNotFound());
    }

    @Test
    void downloadConsenteUnReportPresenteNellaDirectory() throws Exception {
        mvc.perform(get("/report/file").param("nome", "ordini-ottobre.csv"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Ferramenta Bianchi")));
    }

    @Test
    void intestazioneSostituisceSoloIlSegnapostoDataPrevistoEScapaHtml() throws Exception {
        mvc.perform(get("/report/intestazione").param("modello", "Report del ${date:dd/MM/yyyy}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string(matchesPattern("Report del \\d{2}/\\d{2}/\\d{4}")));

        mvc.perform(get("/report/intestazione").param("modello", "${env:PATH}"))
                .andExpect(status().isOk())
                .andExpect(content().string("${env:PATH}"));

        mvc.perform(get("/report/intestazione").param("modello", "<script>alert(1)</script>"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("&lt;script&gt;alert(1)&lt;/script&gt;")))
                .andExpect(content().string(not(containsString("<script>"))));
    }
}
