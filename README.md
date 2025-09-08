# BarberShop - Sistema di Gestione Appuntamenti

## Descrizione del Progetto
BarberShop è un'applicazione desktop sviluppata in Java con JavaFX che permette la gestione completa di un salone di barbiere. Il sistema consente ai barbieri di gestire appuntamenti, servizi e comunicazioni con i clienti, mentre i clienti possono prenotare appuntamenti, scegliere servizi e metodi di pagamento.

## Requisiti di Sistema
- Java 23 o superiore
- PostgreSQL 
- Maven

## Installazione e Configurazione

### Prerequisiti
1. Assicurarsi di avere installato Java 23 o superiore
2. Installare PostgreSQL e creare un database chiamato `BarberShop_DB`
3. Creare un utente PostgreSQL con le seguenti credenziali:
   - Username: `SWEuser`
   - Password: `swepass`
4. Assicurarsi che l'utente abbia i permessi necessari sul database `BarberShop_DB`

### Installazione
1. Clonare il repository
2. Navigare nella directory del progetto
3. Eseguire il comando Maven per compilare il progetto:
   ```
   mvn clean install
   ```
4. Avviare l'applicazione:
   ```
   mvn javafx:run
   ```

## Funzionalità Principali

### Per i Barbieri
- **Gestione Appuntamenti**: Visualizzazione di tutti gli appuntamenti prenotati
- **Gestione Servizi**: Aggiunta, modifica e rimozione dei servizi offerti
- **Comunicazioni**: Invio di comunicazioni ai clienti
- **Profilo**: Gestione delle informazioni personali e professionali

### Per i Clienti
- **Prenotazione Appuntamenti**: Selezione di data, ora, servizi e barbiere
- **Pagamenti**: Possibilità di pagare con diversi metodi (PayPal, Carta di Credito, in negozio)
- **Visualizzazione Appuntamenti**: Storico e appuntamenti futuri
- **Notifiche**: Ricezione di comunicazioni e notifiche di disponibilità

## Come Utilizzare l'Applicazione

### Accesso al Sistema
1. Avviare l'applicazione
2. Nella schermata di login, inserire email e password
3. Selezionare il tipo di utente (Barbiere o Cliente)
4. Cliccare su "Accedi"

### Prenotazione di un Appuntamento (Cliente)
1. Accedere come cliente
2. Navigare alla sezione "Appuntamenti"
3. Cliccare su "Nuovo Appuntamento"
4. Selezionare data, ora, barbiere e servizi desiderati
5. Scegliere il metodo di pagamento
6. Confermare la prenotazione

### Gestione degli Appuntamenti (Barbiere)
1. Accedere come barbiere
2. Navigare alla sezione "Appuntamenti"
3. Visualizzare tutti gli appuntamenti prenotati
4. Possibilità di filtrare per data o cliente

## Struttura del Progetto

### Pacchetti Principali
- **Authentication**: Gestione dell'autenticazione e delle sessioni utente
- **Model**: Classi del modello di dominio (User, Appointment, Service, ecc.)
- **PageControllers**: Controller per le diverse schermate dell'applicazione
- **Payment**: Implementazione dei diversi metodi di pagamento (Strategy Pattern)
- **Persistence**: Accesso al database e persistenza dei dati
- **Services**: Logica di business dell'applicazione

### Pattern di Design Utilizzati
- **Singleton**: Utilizzato per SessionManager e DBManager
- **DAO**: Per l'accesso ai dati persistenti
- **Factory**: Per la creazione di oggetti di pagamento
- **Strategy**: Per implementare diversi metodi di pagamento

## Database
L'applicazione utilizza PostgreSQL come database di produzione. La connessione è configurata con i seguenti parametri:
- URL: `jdbc:postgresql://localhost:5432/BarberShop_DB`
- Username: `SWEuser`
- Password: `swepass`

Per i test viene utilizzato un database H2 in-memory.

## Tecnologie Utilizzate
- **JavaFX**: Framework per l'interfaccia utente
- **MaterialFX**: Libreria di componenti UI
- **PostgreSQL**: Database relazionale
- **JBCrypt**: Per la gestione sicura delle password
- **JUnit, Mockito, TestFX**: Framework per i test