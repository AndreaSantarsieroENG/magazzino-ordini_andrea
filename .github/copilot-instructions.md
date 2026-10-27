# Istruzioni per gli agenti

## Progetto
- Per stack, avvio, architettura e regole di business, consulta [README.md](../README.md); non duplicarne qui i dettagli.
- Per la feature didattica degli sconti, la specifica di riferimento è [M6-G1](../docs/corso/M6-G1.md).
- Non dedurre le regole di dominio dal solo codice: un ordine può passare da CONFERMATO a SPEDITO o ANNULLATO, ma non si annulla dopo la spedizione; l'annullamento restituisce le scorte. Le righe dello stesso prodotto si aggregano prima di calcolare lo sconto e conservano il prezzo al momento dell'ordine. Per gli sconti applica esattamente le soglie 0% (1-9 pezzi), 5% (10-49), 10% (da 50), arrotonda ogni subtotale al centesimo con `HALF_UP` e applica la spedizione da 9,90 € sotto 150,00 € (gratuita da 150,00 €); se la specifica non definisce un caso, chiedi chiarimento invece di inventare una regola.
- Mantieni i confini controller → service → model/repository: i controller gestiscono HTTP e viste, le regole appartengono al model e i service orchestrano i casi d'uso transazionali.
- Preserva le invarianti del dominio e le transazioni, incluso il ripristino dello stato se un ordine fallisce dopo aver elaborato una riga. Usa il `Clock` iniettato per testare date e orari in modo deterministico.
- Segui i pattern esistenti in [OrdineService](../src/main/java/com/epicode/magazzino/service/OrdineService.java) e nei test vicini; evita modifiche non necessarie fuori dal comportamento richiesto.
- Scrivi documentazione e testo delle pagine in italiano, salvo diversa richiesta.

## Verifica
- Esegui `.\mvnw.cmd test` per verificare la suite; per avviare l'app usa `.\mvnw.cmd spring-boot:run`.
- Per convenzioni e casi limite dei test, segui [le istruzioni dedicate](instructions/test.instructions.md).
- Una feature non è finita senza test che ne verificano le regole.
