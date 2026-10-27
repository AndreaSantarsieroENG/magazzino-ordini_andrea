	## Piano: Sconti per quantità e spedizione

	Implementare le regole R1–R5 nel model, mostrare i risultati nelle viste per R6 e aggiungere test di dominio e delle pagine. L’aggregazione delle righe e il prezzo fissato al momento dell’ordine esistono già e vanno preservati.

	### Passi

	1. **Sconto per riga:** in `RigaOrdine`, calcolare la percentuale dalla quantità aggregata: 0% per 1–9 pezzi, 5% per 10–49, 10% da 50. Calcolare il subtotale scontato e arrotondarlo ai centesimi con `HALF_UP`; rendere disponibile la percentuale alla vista.
	2. **Totali dell’ordine:** in `Ordine`, mantenere `totale()` come totale merce scontato. Aggiungere la spedizione di 9,90 € se il totale merce è inferiore a 150,00 €, altrimenti zero, e un metodo per il totale da pagare. La soglia si applica al totale merce scontato.
	3. **Viste:** in `ordine-dettaglio.html`, mostrare la percentuale per riga e distinguere totale merce, spedizione e totale da pagare. In `ordini.html`, mostrare il totale da pagare.
	4. **Test di dominio:** estendere `OrdineTest` per le soglie 9/10 e 49/50, l’aggregazione 5+5 che attiva lo sconto, l’arrotondamento `HALF_UP`, la somma dei subtotali scontati e i confini di spedizione 149,99/150,00. Conservare i test sulle invarianti dell’ordine e sul prezzo.
	5. **Test delle pagine:** estendere `PagineTest` con verifiche MockMvc per la percentuale e gli importi mostrati nel dettaglio e nell’elenco.
	6. **Verifica:** eseguire `.\mvnw.cmd test`.

	### File interessati

	- `src/main/java/com/epicode/magazzino/model/RigaOrdine.java`: percentuale e subtotale scontato.
	- `src/main/java/com/epicode/magazzino/model/Ordine.java`: totale merce, spedizione e totale da pagare; l’aggregazione in `aggiungiRiga()` esiste già.
	- `src/main/resources/templates/ordine-dettaglio.html`: percentuale e riepilogo degli importi.
	- `src/main/resources/templates/ordini.html`: totale da pagare nell’elenco.
	- `src/test/java/com/epicode/magazzino/model/OrdineTest.java`: regole e confini del dominio.
	- `src/test/java/com/epicode/magazzino/web/PagineTest.java`: importi e sconto visualizzati.
	- `src/test/java/com/epicode/magazzino/service/OrdineServiceTest.java`: mantenere la verifica esistente del totale merce; non dovrebbe richiedere modifiche se `totale()` conserva il significato attuale.

	### Decisioni

	- La specifica [M6-G1](../corso/M6-G1.md) è la fonte di verità. Se un caso non è definito, chiedere chiarimenti invece di inventare una regola.
	- Conservare l’aggregazione per prodotto e il prezzo unitario al momento dell’ordine.
	- Calcolare la spedizione sul totale merce dopo gli sconti.
	- Mantenere le regole nel model; non modificare controller o service salvo necessità emersa durante l’implementazione. Preservare transazioni, rollback e gestione dello stato.
    