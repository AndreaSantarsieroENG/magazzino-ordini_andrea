---
name: Test Instructions
description: "Use when adding or changing tests under src/test; covers JUnit 5 unit, integration, and MockMvc test patterns and behavioral test design."
applyTo: "src/test/**"
---

# Istruzioni per i test

- Segui gli esempi di [test di dominio](../../src/test/java/com/epicode/magazzino/model/OrdineTest.java), [test di integrazione dei service](../../src/test/java/com/epicode/magazzino/service/OrdineServiceTest.java) e [test delle pagine](../../src/test/java/com/epicode/magazzino/web/PagineTest.java).
- Preferisci test di dominio JUnit 5 senza avviare Spring; usa `@SpringBootTest` per verificare service, repository e H2, e MockMvc per le richieste web. I test di integrazione esistenti usano `@Transactional`.
- Verifica comportamenti osservabili con risultati attesi indipendenti dall'implementazione; evita test tautologici o che riproducono soltanto la logica di produzione.
- Copri i valori limite e gli errori rilevanti, verificando anche gli effetti sullo stato (per esempio scorte e stato dell'ordine) e il rollback quando applicabile.
- Per importi usa `BigDecimal` con valori decimali espliciti, come negli esempi esistenti; non usare `double`.
- Mantieni nomi descrittivi in italiano e testa una regola per volta, aggiungendo casi combinati solo quando servono a dimostrare un requisito.