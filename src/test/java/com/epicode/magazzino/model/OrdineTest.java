package com.epicode.magazzino.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdineTest {

    private static final LocalDateTime ADESSO = LocalDateTime.of(2026, 10, 26, 9, 0);

    private Prodotto viti;
    private Prodotto trapano;

    @BeforeEach
    void catalogo() {
        viti = new Prodotto("VIT", "Viti", new BigDecimal("6.90"), 100, 10);
        trapano = new Prodotto("TRA", "Trapano", new BigDecimal("89.00"), 5, 1);
    }

    @Test
    void ilTotaleESommaDeiSubtotali() {
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(viti, 3); // 20,70
        ordine.aggiungiRiga(trapano, 2); // 178,00
        assertEquals(new BigDecimal("198.70"), ordine.totale());
    }

    @Test
    void applicaLeSoglieDiScontoAllaQuantitaDellaRiga() {
        Prodotto prodottoNove = new Prodotto("V9", "Viti", new BigDecimal("6.90"), 100, 0);
        Prodotto prodottoDieci = new Prodotto("V10", "Viti", new BigDecimal("6.90"), 100, 0);
        Prodotto prodottoQuarantanove = new Prodotto("V49", "Viti", new BigDecimal("6.90"), 100, 0);
        Prodotto prodottoCinquanta = new Prodotto("V50", "Viti", new BigDecimal("6.90"), 100, 0);
        Ordine ordine = new Ordine("Rossi", ADESSO);

        ordine.aggiungiRiga(prodottoNove, 9);
        ordine.aggiungiRiga(prodottoDieci, 10);
        ordine.aggiungiRiga(prodottoQuarantanove, 49);
        ordine.aggiungiRiga(prodottoCinquanta, 50);

        assertEquals(0, ordine.getRighe().get(0).getPercentualeSconto());
        assertEquals(new BigDecimal("62.10"), ordine.getRighe().get(0).subtotale());
        assertEquals(5, ordine.getRighe().get(1).getPercentualeSconto());
        assertEquals(new BigDecimal("65.55"), ordine.getRighe().get(1).subtotale());
        assertEquals(5, ordine.getRighe().get(2).getPercentualeSconto());
        assertEquals(new BigDecimal("321.20"), ordine.getRighe().get(2).subtotale());
        assertEquals(10, ordine.getRighe().get(3).getPercentualeSconto());
        assertEquals(new BigDecimal("310.50"), ordine.getRighe().get(3).subtotale());
    }

    @Test
    void laQuantitaAggregataDeterminaLoSconto() {
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(viti, 5);
        ordine.aggiungiRiga(viti, 5);

        RigaOrdine riga = ordine.getRighe().get(0);
        assertEquals(10, riga.getQuantita());
        assertEquals(5, riga.getPercentualeSconto());
        assertEquals(new BigDecimal("65.55"), riga.subtotale());
    }

    @Test
    void ilSubtotaleScontatoSiArrotondaConHalfUp() {
        Prodotto prodotto = new Prodotto("CEN", "Viti", new BigDecimal("0.05"), 10, 0);
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(prodotto, 10);

        assertEquals(new BigDecimal("0.48"), ordine.getRighe().get(0).subtotale());
    }

    @Test
    void totaleMerceSommaISubtotaliScontati() {
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(viti, 10);
        ordine.aggiungiRiga(trapano, 2);

        assertEquals(new BigDecimal("243.55"), ordine.totale());
    }

    @Test
    void applicaLaSpedizioneSottoCentocinquantaEuroEGratisAllaSoglia() {
        Ordine ordineSottoSoglia = new Ordine("Rossi", ADESSO);
        ordineSottoSoglia.aggiungiRiga(
                new Prodotto("SOTTO", "Prodotto", new BigDecimal("149.99"), 1, 0), 1);
        Ordine ordineAllaSoglia = new Ordine("Rossi", ADESSO);
        ordineAllaSoglia.aggiungiRiga(
                new Prodotto("SOGLIA", "Prodotto", new BigDecimal("150.00"), 1, 0), 1);

        assertEquals(new BigDecimal("149.99"), ordineSottoSoglia.totale());
        assertEquals(new BigDecimal("9.90"), ordineSottoSoglia.spedizione());
        assertEquals(new BigDecimal("159.89"), ordineSottoSoglia.totaleDaPagare());
        assertEquals(new BigDecimal("150.00"), ordineAllaSoglia.totale());
        assertEquals(new BigDecimal("0.00"), ordineAllaSoglia.spedizione());
        assertEquals(new BigDecimal("150.00"), ordineAllaSoglia.totaleDaPagare());
    }

    @Test
    void calcolaLaSpedizioneSulTotaleMerceScontato() {
        Prodotto prodotto = new Prodotto("SC15", "Prodotto", new BigDecimal("15.00"), 10, 0);
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(prodotto, 10);

        assertEquals(new BigDecimal("142.50"), ordine.totale());
        assertEquals(new BigDecimal("9.90"), ordine.spedizione());
        assertEquals(new BigDecimal("152.40"), ordine.totaleDaPagare());
    }

    @Test
    void ogniRigaPrelevaDalMagazzino() {
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(trapano, 2);
        assertEquals(3, trapano.getDisponibili());
    }

    @Test
    void loStessoProdottoDueVolteFiniscenellaStessaRiga() {
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(viti, 3);
        ordine.aggiungiRiga(viti, 2);
        assertEquals(1, ordine.getRighe().size());
        assertEquals(5, ordine.getRighe().get(0).getQuantita());
    }

    @Test
    void ilPrezzoDellaRigaRestaQuelloDelMomentoDellOrdine() {
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(viti, 1);
        assertEquals(new BigDecimal("6.90"), ordine.getRighe().get(0).getPrezzoUnitario());
    }

    @Test
    void annullareRestituisceIPezzi() {
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(trapano, 2);
        ordine.annulla();
        assertEquals(StatoOrdine.ANNULLATO, ordine.getStato());
        assertEquals(5, trapano.getDisponibili());
    }

    @Test
    void unOrdineSpeditoNonSiAnnulla() {
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(trapano, 1);
        ordine.spedisci();
        assertThrows(OperazioneNonConsentitaException.class, ordine::annulla);
        assertEquals(4, trapano.getDisponibili());
    }

    @Test
    void unOrdineAnnullatoNonSiAnnullaDueVolte() {
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(trapano, 1);
        ordine.annulla();
        assertThrows(OperazioneNonConsentitaException.class, ordine::annulla);
        assertEquals(5, trapano.getDisponibili());
    }

    @Test
    void nonSiAggiungonoRigheAUnOrdineSpedito() {
        Ordine ordine = new Ordine("Rossi", ADESSO);
        ordine.aggiungiRiga(viti, 1);
        ordine.spedisci();
        assertThrows(OperazioneNonConsentitaException.class, () -> ordine.aggiungiRiga(viti, 1));
    }

    @Test
    void clienteObbligatorio() {
        assertThrows(RichiestaNonValidaException.class, () -> new Ordine("  ", ADESSO));
    }
}
