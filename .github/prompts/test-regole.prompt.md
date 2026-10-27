---
name: Test delle regole
description: "Aggiunge o verifica test comportamentali per la specifica degli sconti e della spedizione M6-G1."
argument-hint: "Regola o classe da approfondire (facoltativo)"
agent: "agent"
---

Leggi [la specifica M6-G1](../../docs/corso/M6-G1.md), poi esamina l'implementazione e i test esistenti prima di intervenire. La specifica è la fonte di verità; rispetta le convenzioni in [test.instructions.md](../instructions/test.instructions.md).

Completa o correggi i test comportamentali per le regole R1-R6. Assicurati in particolare che:
- ogni regola R1-R5 abbia almeno un test;
- siano coperti i confini di quantità 9/10 e 49/50, e di totale merce 149,99/150,00;
- R3 verifichi il caso in cui lo stesso prodotto viene aggiunto in più chiamate e la quantità aggregata determina lo sconto;
- R6 verifichi le informazioni mostrate nel dettaglio e nell'elenco ordini;
- i test non siano tautologici e falliscano se una soglia o un risultato atteso viene alterato.

Modifica solo i test: non cambiare il codice di produzione per farli passare. Se la specifica rivela un comportamento mancante, lascia il test che lo dimostra e segnala il punto. Esegui `.\mvnw.cmd test` e riporta i file modificati, le regole coperte e l'esito.